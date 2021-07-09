package org.apiaddicts.apitools.apigen.generatorrest.uitls;

import org.apiaddicts.apitools.apigen.generatorrest.core.exceptions.GeneratorRestErrors;
import org.apiaddicts.apitools.apigen.generatorrest.core.exceptions.GeneratorRestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenAPIZipUtils {

    private OpenAPIZipUtils() {
        // Intentional blank
    }

    public static File getZipRootFile(MultipartFile zip) throws IOException {
        Path tempDir = org.apiaddicts.apitools.apigen.generatorcore.utils.ZipUtils.unzip(zip.getBytes());
        try (Stream<File> stream = Files.walk(tempDir).filter(Files::isRegularFile).map(Path::toFile)) {
            List<File> files = stream.collect(Collectors.toList());
            if (files.isEmpty()) {
                throw new GeneratorRestException(GeneratorRestErrors.ZIP_EMPTY);
            } else {
                if (files.size() > 1) {
                    return files.stream()
                            .filter(f -> f.getName().equals("api.json") || f.getName().equals("api.yaml"))
                            .findFirst().orElseThrow(() -> new GeneratorRestException(GeneratorRestErrors.ZIP_MULTIPLE_ROOT));
                } else {
                    return files.get(0);
                }
            }
        }
    }
}
