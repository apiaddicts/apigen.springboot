package org.apiaddicts.apitools.apigen.generatorcli.config;

import org.springframework.core.annotation.Order;
import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;

@Order(0)
public class CustomExceptionResolver implements CommandExceptionResolver {

    @Override
    public CommandHandlingResult resolve(Exception ex) {
        return CommandHandlingResult.of(ex.getMessage(), 1);
    }
}
