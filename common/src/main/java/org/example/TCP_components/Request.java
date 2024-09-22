package org.example.TCP_components;

import java.io.Serializable;

public class Request implements Serializable {
    private String commandName;
    private String serializedUser;
    private String serializedRequestBody;

    public Request(String commandName, String serializedUser, String serializedRequestBody) {
        this.commandName = commandName;
        this.serializedUser = serializedUser;
        this.serializedRequestBody = serializedRequestBody;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getSerializedUser() {
        return serializedUser;
    }

    public String getSerializedRequestBody() {
        return serializedRequestBody;
    }

    @Override
    public String toString() {
        return "Request{" +
                "commandName='" + commandName + '\'' +
                ", serializedUser='" + serializedUser + '\'' +
                ", serializedRequestBody='" + serializedRequestBody + '\'' +
                '}';
    }
}
