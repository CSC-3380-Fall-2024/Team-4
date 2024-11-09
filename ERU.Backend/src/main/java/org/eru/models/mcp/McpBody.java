package org.eru.models.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class McpBody {
    @JsonProperty("affiliateName")
    public String affiliateName;

    @JsonProperty("currency")
    public String Currency;

    @JsonProperty("offerId")
    public String OfferId;

    @JsonProperty("purchaseQuantity")
    public int PurchaseQuantity;
}
