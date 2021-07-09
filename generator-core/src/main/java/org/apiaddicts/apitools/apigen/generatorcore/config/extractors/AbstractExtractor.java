package org.apiaddicts.apitools.apigen.generatorcore.config.extractors;

import org.apiaddicts.apitools.apigen.generatorcore.config.extractors.context.ExtractorContext;

public abstract class AbstractExtractor {
    protected final ExtractorContext context;

    public AbstractExtractor(ExtractorContext context) {
        this.context = context;
    }
}
