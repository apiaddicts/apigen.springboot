package org.apiaddicts.apitools.apigen.generatorcore.generator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@AllArgsConstructor
public class Project {
    String name;
    ByteArrayResource content;
}
