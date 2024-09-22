package org.example.TCP_components;

import java.io.Serializable;

public class Response implements Serializable {
    private boolean status;
    private String textStatus;
    private String responseBody;

    public Response(boolean status, String textStatus, String responseBody) {
        this.status = status;
        this.textStatus = textStatus;
        this.responseBody = responseBody;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTextStatus() {
        return textStatus;
    }

    public void setTextStatus(String textStatus) {
        this.textStatus = textStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", textStatus='" + textStatus + '\'' +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
