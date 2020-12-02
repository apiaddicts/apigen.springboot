package net.cloudappi.apigen.archetypecore.exceptions;

public class RelationalErrorsException extends RuntimeException {
    private final RelationalErrors relationalErrors;

    public RelationalErrorsException(RelationalErrors relationalErrors) {
        this.relationalErrors = relationalErrors;
    }

    public RelationalErrors getRelationalErrors() {
        return relationalErrors;
    }
}
