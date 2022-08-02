package org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.service;

import lombok.extern.slf4j.Slf4j;
import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.common.service.ServicesGenerator;

@Slf4j
public class ApigenServicesGenerator<C extends ApigenContext> extends ServicesGenerator<C> {

    public ApigenServicesGenerator(C ctx, Configuration cfg) {
        super(ctx, cfg, ApigenServiceBuilder::new);
    }
}
