package org.example.interfaces;

import org.example.TCP_components.Request;
import org.example.cmd.utils.Ask;
import org.example.exceptions.ExecutionException;

public interface ClientExecutable {
    public Request apply(String userCommandArgument, boolean isScript) throws Ask.AskBreak, ExecutionException;
}
