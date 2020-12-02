package net.cloudappi.apigen.generatorrest.web;

import net.cloudappi.apigen.generatorcore.config.Configuration;
import net.cloudappi.apigen.generatorcore.generator.ApigenProjectGenerator;
import net.cloudappi.apigen.generatorcore.utils.ZipUtils;
import net.cloudappi.apigen.generatorrest.uitls.OpenAPIZipUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/configuration")
public class ConfigController {

    private final ApigenProjectGenerator generator;

    public ConfigController(ApigenProjectGenerator generator) {
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
