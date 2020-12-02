package net.cloudappi.apigen.generatorcore.exceptions;

public enum GeneratorErrors {
    MODEL_WITHOUT_RELATIONAL_PERSISTENCE(1, "Model %s without relational-persistence"),
    MODEL_WITHOUT_ATTRIBUTES(2, "Model %s without attributes"),
    MODEL_RELATIONAL_PERSISTENCE_WITHOUT_TABLE(3, "Model %s without table in relational-persistence"),
    MODEL_ATTRIBUTE_WITHOUT_NAME(4, "Model %s with attribute without name"),
    MODEL_ATTRIBUTE_WITHOUT_TYPE(5, "Model %s attribute %s without type"),
    MODEL_ATTRIBUTE_WITHOUT_RELATIONAL_PERSISTENCE(6, "Model attribute %s without relational-persistence"),
    EXTRACTOR_ERROR(99, "Extractor error"),
    CONFIGURATION_NOT_VALID(100, "Configuration not valid")
    ;

    public final int code;
    public final String message;

    GeneratorErrors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
