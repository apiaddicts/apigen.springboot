package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.mapper.MappersGenerator;


@Slf4j
public class ApigenMappersGenerator<C extends ApigenContext> extends MappersGenerator<C> {

    public ApigenMappersGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg, ApigenMapperBuilder::new);
    }
}
