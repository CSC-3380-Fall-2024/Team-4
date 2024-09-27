package org.eru.models.websocket;

import org.eru.Variables;
import org.java_websocket.WebSocket;

public class Client {
    private final WebSocket ws;
    private String accountId;
    private String jid;
    private String id;
    private boolean authenticated;
    private String lastPresenceStatus;
    private boolean away;

    public Client(WebSocket ws) {
        this.ws = ws;
        this.accountId = "";
        this.jid = "";
        this.id = "";
        this.authenticated = false;
        this.lastPresenceStatus = "{}";
        this.away = false;
    }

    public WebSocket getWs() {
        return ws;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getLastPresenceStatus() {
        return lastPresenceStatus;
    }

    public void setLastPresenceStatus(String lastPresenceStatus) {
        this.lastPresenceStatus = lastPresenceStatus;
    }

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean away) {
        this.away = away;
    }

    public void close() {
        Variables.WSConnection.onClose(ws, 0, null, false);
    }
}
