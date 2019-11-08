package com.cy.util.cmd;

/**
* LocalCommandExecutor.java
*/
public interface LocalCommandExecutor {
    ExecuteResult executeCommand(String command, long timeout);
}