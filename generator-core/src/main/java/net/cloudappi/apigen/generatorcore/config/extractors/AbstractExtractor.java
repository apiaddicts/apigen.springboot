package net.cloudappi.apigen.generatorcore.config.extractors;

import net.cloudappi.apigen.generatorcore.config.extractors.context.ExtractorContext;

public abstract class AbstractExtractor {
    protected final ExtractorContext context;

    public AbstractExtractor(ExtractorContext context) {
        this.context = context;
    }
}
