package net.cloudappi.apigen.generatorrest.core.exceptions;

public enum GeneratorRestErrors {
    ZIP_EMPTY(1000, "Zip empty"),
    ZIP_MULTIPLE_ROOT(1001, "Zip file with multiple root files and none named 'api'"),
    UNEXPECTED_ERROR(2000, "Unexpected error")
    ;

    public final int code;
    public final String message;

    GeneratorRestErrors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
