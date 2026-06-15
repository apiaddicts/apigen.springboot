package org.apiaddicts.apitools.apigen.generatorcli.config;

import org.springframework.shell.core.command.ExitStatus;
import org.springframework.shell.core.command.exit.ExitStatusExceptionMapper;

public class CustomExceptionResolver implements ExitStatusExceptionMapper {

    @Override
    public ExitStatus apply(Exception ex) {
        return new ExitStatus(1, ex.getMessage());
    }
}
