package org.eru.models.websocket;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.*;

import org.eru.models.websocket.Client;
import org.bson.json.JsonObject;
import org.bson.json.JsonParseException;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMPP extends WebSocketServer {
    private static final int PORT = 80;
    private final List<Client> clients = new ArrayList<>();

    public List<Client> getClients() {
        return clients;
    }

    public XMPP() {
        super(new InetSocketAddress(PORT));
    }

    public void startServer() {
        try {
            start();
        } catch (Exception e) {
            System.err.println("XMPP FAILED to start listening.");
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake handshake) {
        ws.setAttachment(new Client(ws));
    }

    @Override
    public void onClose(WebSocket ws, int code, String reason, boolean remote) {
        removeClient(ws);
    }

    @Override
    public void onMessage(WebSocket ws, String message) {
        Client client = ws.getAttachment();

        try {
            Document doc = parseXML(message);
            if (doc == null) {
                sendError(ws);
                return;
            }

            Element root = doc.getDocumentElement();
            switch (root.getNodeName()) {
                case "open" -> handleOpen(ws, client, root);
                case "auth" -> handleAuth(ws, client, root);
                case "iq" -> handleIQ(ws, client, root);
                case "message" -> handleMessage(ws, client, root);
                case "presence" -> handlePresence(ws, client, root);
                default -> sendError(ws);
            }

            if (!clients.contains(client) && client.isAuthenticated()) {
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(ws);
        }
    }

    @Override
    public void onError(WebSocket ws, Exception ex) {
        System.err.println("An error occurred in a websocket: " + ex.getMessage());
    }
    @Override
    public void onStart() {
        System.out.println("XMPP started listening on port " + PORT);
    }

    private void handleOpen(WebSocket ws, Client client, Element root) {
        String id = UUID.randomUUID().toString();
        client.setId(id);

        ws.send(buildOpenResponse(id));
        ws.send(buildFeaturesResponse(client.isAuthenticated()));
    }

    private void handleAuth(WebSocket ws, Client client, Element root) {
        String content = root.getTextContent();
        if (content == null || content.isEmpty()) {
            sendError(ws);
            return;
        }

        String decoded = new String(Base64.getDecoder().decode(content));
        String[] parts = decoded.split("\u0000");
        if (parts.length != 3) {
            sendError(ws);
            return;
        }

        String accountId = parts[1];
        if (clients.stream().anyMatch(c -> c.getAccountId().equals(accountId))) {
            sendError(ws);
            return;
        }

        client.setAccountId(accountId);
        client.setAuthenticated(true);

        System.out.println("An XMPP client with the account ID " + accountId + " has logged in.");
        ws.send(buildAuthSuccessResponse());
    }

    private void handleIQ(WebSocket ws, Client client, Element root) {
        String id = root.getAttribute("id");
        switch (id) {
            case "_xmpp_bind1" -> handleBind(ws, client, root);
            case "_xmpp_session1" -> handleSession(ws, client, root);
            default -> handleDefaultIQ(ws, client, root);
        }
    }

    private void handleMessage(WebSocket ws, Client client, Element root) {
        String type = root.getAttribute("type");
        if (type.equals("chat")) {
            handleChatMessage(ws, client, root);
        } else {
            handleJSONMessage(ws, client, root);
        }
    }

    private void handlePresence(WebSocket ws, Client client, Element root) {
        String status = getElementContent(root, "status");
        if (status != null && isJSON(status)) {
            boolean away = getElementContent(root, "show") != null;
            updatePresenceForAll(ws, status, away, false);
        } else {
            sendError(ws);
        }
    }

    private void handleBind(WebSocket ws, Client client, Element root) {
        String resource = getElementContent(root, "resource");
        if (resource != null) {
            String jid = client.getAccountId() + "@prod.ol.epicgames.com/" + resource;
            client.setJid(jid);
            ws.send(buildBindResponse(jid));
        } else {
            sendError(ws);
        }
    }

    private void handleSession(WebSocket ws, Client client, Element root) {
        ws.send(buildSessionResponse(client.getJid()));
        getPresenceForAll(ws);
    }

    private void handleDefaultIQ(WebSocket ws, Client client, Element root) {
        ws.send(buildDefaultIQResponse(client.getJid(), root.getAttribute("id")));
    }

    private void handleChatMessage(WebSocket ws, Client client, Element root) {
        String to = root.getAttribute("to");
        Optional<Client> receiverOpt = clients.stream().filter(c -> c.getJid().equals(to)).findFirst();
        if (receiverOpt.isPresent()) {
            Client receiver = receiverOpt.get();
            receiver.getWs().send(buildChatMessage(client.getJid(), receiver.getJid(), getElementContent(root, "body")));
        } else {
            sendError(ws);
        }
    }

    private void handleJSONMessage(WebSocket ws, Client client, Element root) {
        String body = getElementContent(root, "body");
        if (isJSON(body)) {
            // TODO: Handle JSON
        } else {
            sendError(ws);
        }
    }

    private void removeClient(WebSocket ws) {
        Client client = clients.stream().filter(c -> c.getWs().equals(ws)).findFirst().orElse(null);
        if (client != null) {
            updatePresenceForAll(ws, "{}", false, true);
            System.out.println(("An XMPP client with the account ID " + client.getAccountId() + " has logged out."));
            clients.remove(client);
        }
    }

    private void sendError(WebSocket ws) {
        ws.send("<close xmlns='urn:ietf:params:xml:ns:xmpp-framing'/>");
        ws.close();
    }

    private void updatePresenceForAll(WebSocket ws, String status, boolean away, boolean offline) {
        Client sender = clients.stream().filter(c -> c.getWs().equals(ws)).findFirst().orElse(null);
        if (sender != null) {
            clients.forEach(client -> {
                if (client.getWs().isOpen()) {
                    client.getWs().send(buildPresenceResponse(sender.getJid(), client.getJid(), status, away, offline));
                }
                else {
                    System.out.println("WebSocket: " + client.getJid() + " is closed!");
                }
            });
        } else {
            sendError(ws);
        }
    }

    private void getPresenceForAll(WebSocket ws) {
        Client sender = clients.stream().filter(c -> c.getWs().equals(ws)).findFirst().orElse(null);
        if (sender != null) {
            clients.forEach(client -> {
                ws.send(buildPresenceResponse(client.getJid(), sender.getJid(), client.getLastPresenceStatus(), client.isAway(), false));
            });
        } else {
            sendError(ws);
        }
    }

    private Document parseXML(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getElementContent(Element parent, String tagName) {
        NodeList elements = parent.getElementsByTagName(tagName);
        return elements.getLength() > 0 ? elements.item(0).getTextContent() : null;
    }

    private boolean isJSON(String str) {
        try {
            new JsonObject(str);
        } catch (JsonParseException ex) {
            return false;
        }

        return true;
    }

    private String buildOpenResponse(String id) {
        return "<open xmlns='urn:ietf:params:xml:ns:xmpp-framing' from='prod.ol.eru.org' id='" + id + "' version='1.0' xml:lang='en'/>";
    }

    private String buildAuthSuccessResponse() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element success = doc.createElement("success");
            success.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-sasl");
            doc.appendChild(success);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildFeaturesResponse(boolean authenticated) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element features = doc.createElement("stream:features");
            features.setAttribute("xmlns:stream", "http://etherx.jabber.org/streams");
            doc.appendChild(features);

            if (authenticated) {
                Element ver = doc.createElement("ver");
                ver.setAttribute("xmlns", "urn:xmpp:features:rosterver");
                features.appendChild(ver);

                Element starttls = doc.createElement("starttls");
                starttls.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-tls");
                features.appendChild(starttls);

                Element bind = doc.createElement("bind");
                bind.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-bind");
                features.appendChild(bind);

                Element compression = doc.createElement("compression");
                compression.setAttribute("xmlns", "http://jabber.org/features/compress");
                Element method = doc.createElement("method");
                method.setTextContent("zlib");
                compression.appendChild(method);
                features.appendChild(compression);

                Element session = doc.createElement("session");
                session.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-session");
                features.appendChild(session);
            } else {
                Element mechanisms = doc.createElement("mechanisms");
                mechanisms.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-sasl");
                Element mechanism = doc.createElement("mechanism");
                mechanism.setTextContent("PLAIN");
                mechanisms.appendChild(mechanism);
                features.appendChild(mechanisms);

                Element ver = doc.createElement("ver");
                ver.setAttribute("xmlns", "urn:xmpp:features:rosterver");
                features.appendChild(ver);

                Element starttls = doc.createElement("starttls");
                starttls.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-tls");
                features.appendChild(starttls);

                Element compression = doc.createElement("compression");
                compression.setAttribute("xmlns", "http://jabber.org/features/compress");
                Element method = doc.createElement("method");
                method.setTextContent("zlib");
                compression.appendChild(method);
                features.appendChild(compression);

                Element auth = doc.createElement("auth");
                auth.setAttribute("xmlns", "http://jabber.org/features/iq-auth");
                features.appendChild(auth);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildPresenceResponse(String fromJid, String toJid, String status, boolean away, boolean offline) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element presence = doc.createElement("presence");
            presence.setAttribute("from", fromJid);
            presence.setAttribute("to", toJid);
            presence.setAttribute("xmlns", "jabber:client");

            presence.setAttribute("type", offline ? "unavailable" : "available");

            if (away) {
                Element show = doc.createElement("show");
                show.appendChild(doc.createTextNode("away"));
                presence.appendChild(show);
            }

            Element statusElement = doc.createElement("status");
            statusElement.appendChild(doc.createTextNode(status));
            presence.appendChild(statusElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildBindResponse(String jid) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element iq = doc.createElement("iq");
            iq.setAttribute("to", jid);
            iq.setAttribute("id", "_xmpp_bind1");
            iq.setAttribute("xmlns", "jabber:client");
            iq.setAttribute("type", "result");
            doc.appendChild(iq);

            Element bind = doc.createElement("bind");
            bind.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-bind");
            iq.appendChild(bind);

            Element jidElement = doc.createElement("jid");
            jidElement.setTextContent(jid);
            bind.appendChild(jidElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildSessionResponse(String jid) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element iq = doc.createElement("iq");
            iq.setAttribute("to", jid);
            iq.setAttribute("from", "prod.ol.epicgames.com");
            iq.setAttribute("id", "_xmpp_session1");
            iq.setAttribute("xmlns", "jabber:client");
            iq.setAttribute("type", "result");
            doc.appendChild(iq);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildDefaultIQResponse(String jid, String id) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element iq = doc.createElement("iq");
            iq.setAttribute("to", jid);
            iq.setAttribute("from", "prod.ol.epicgames.com");
            iq.setAttribute("id", id);
            iq.setAttribute("xmlns", "jabber:client");
            iq.setAttribute("type", "result");
            doc.appendChild(iq);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildChatMessage(String from, String to, String body) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element message = doc.createElement("message");
            message.setAttribute("to", to);
            message.setAttribute("from", from);
            message.setAttribute("xmlns", "jabber:client");
            message.setAttribute("type", "chat");
            doc.appendChild(message);

            Element bodyElement = doc.createElement("body");
            bodyElement.setTextContent(body);
            message.appendChild(bodyElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
