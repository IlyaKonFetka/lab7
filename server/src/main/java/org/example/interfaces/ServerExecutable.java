package org.example.interfaces;

import org.example.TCP_components.Response;

public interface ServerExecutable {
    public Response apply(String commandArgument, long userID);
}
