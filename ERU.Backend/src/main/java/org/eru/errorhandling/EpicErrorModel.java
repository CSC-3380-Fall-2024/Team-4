package org.eru.errorhandling;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EpicErrorModel {
    @JsonProperty("errorCode")
    public String ErrorCode;

    @JsonProperty("errorMessage")
    public String ErrorMessage;

    @JsonProperty("messageVars")
    public String[] MessageVars;

    @JsonProperty("numericErrorCode")
    public int NumericErrorCode;

    @JsonProperty("originatingService")
    public String OriginatingService = "eru";

    @JsonProperty("intent")
    public String Intent = "prod";

    @JsonProperty("error_description")
    public String ErrorDescription;

    @JsonProperty("error")
    public String Error;

    public EpicErrorModel(String errorCode, String errorMessage, String[] messageVars, int numericErrorCode) {
        ErrorCode = errorCode;
        ErrorMessage = errorMessage;
        MessageVars = messageVars;
        NumericErrorCode = numericErrorCode;
        ErrorDescription = errorMessage;

        String[] split = errorCode.split("\\.");
        Error = split[split.length - 1];
    }
}
