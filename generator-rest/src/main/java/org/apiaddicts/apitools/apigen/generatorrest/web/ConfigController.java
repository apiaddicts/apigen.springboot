package org.apiaddicts.apitools.apigen.generatorrest.web;

import org.apiaddicts.apitools.apigen.generatorcore.config.Configuration;
import org.apiaddicts.apitools.apigen.generatorcore.generator.ProjectGenerator;
import org.apiaddicts.apitools.apigen.generatorcore.generator.implementations.java.apigen.ApigenContext;
import org.apiaddicts.apitools.apigen.generatorcore.utils.ZipUtils;
import org.apiaddicts.apitools.apigen.generatorrest.uitls.OpenAPIZipUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/configuration")
public class ConfigController {

    private final ProjectGenerator<ApigenContext> generator;

    public ConfigController(ProjectGenerator<ApigenContext> generator) {
        this.generator = generator;
    }

    @PostMapping(value = "/file")
    public Configuration fromFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (ZipUtils.isZip(file)) {
            File rootFile = OpenAPIZipUtils.getZipRootFile(file);
            return generator.getConfiguration(rootFile.toPath());
        } else {
            return generator.getConfiguration(file.getBytes());
        }
    }


}
