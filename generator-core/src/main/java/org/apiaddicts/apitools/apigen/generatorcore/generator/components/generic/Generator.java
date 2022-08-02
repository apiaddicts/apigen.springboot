package org.apiaddicts.apitools.apigen.generatorcore.generator.components.generic;

import java.io.IOException;
import java.nio.file.Path;

public interface Generator {

    void generate(Path projectPath) throws IOException;
    
}