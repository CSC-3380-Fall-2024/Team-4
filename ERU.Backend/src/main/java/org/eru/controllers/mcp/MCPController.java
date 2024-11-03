package org.eru.controllers.mcp;

import org.eru.errorhandling.exceptions.account.AccountNotFoundException;
import org.eru.errorhandling.exceptions.account.InvalidAccountCredentials;
import org.eru.errorhandling.exceptions.eru.IdInvalidException;
import org.eru.models.mcp.McpBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class MCPController {
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/eru/api/game/v2/profile/{accountId}/client/{operation}")
    public ResponseEntity<McpResponse> client(
            @PathVariable String accountId,
            @PathVariable String operation,
            @RequestParam(value = "profileId", required = false) String profile,
            @RequestParam(value = "rvn", required = false) Integer queryRevision,
            @RequestBody(required = false) McpBody body
            )
            throws AccountNotFoundException, IdInvalidException, InvalidAccountCredentials {
        String profileId = profile == null ? "common_core" : profile;

        //MongoDBManager.getInstance().removeUserByAccountId("Tamely");
        //MongoDBManager.getInstance().PushAccount();

        /*
        case "ClientQuestLogin", "GetMcpTimeForLogin", "RefreshExpeditions", "IncrementNamedCounterStat"
         */

        /*
        switch (operation) {
            case "QueryProfile" -> {
                switch (profileId) {
                    case "profile0", "common_core" -> { return CommonCore.execute(accountId); }
                    case "athena" -> { return Athena.execute(accountId); }
                    default -> { return ResponseEntity.ok(new McpResponse(queryRevision, profileId, new ArrayList<>())); }
                }
            }
            case "ClientQuestLogin", "SetHardcoreModifier" -> { return ResponseEntity.ok(new McpResponse(queryRevision, profileId, new ArrayList<>())); }
            case "SetAffiliateName" -> { return SetAffiliateName.execute(body, profileId, accountId); }
            case "PurchaseCatalogEntry" -> { return PurchaseCatalogEntry.execute(body, accountId, profileId, queryRevision); }
            default -> throw new InvalidAccountCredentials();
        }*/

        throw new InvalidAccountCredentials();
    }
}
