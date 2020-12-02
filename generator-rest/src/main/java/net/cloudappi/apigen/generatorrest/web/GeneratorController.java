package net.cloudappi.apigen.generatorrest.web;

import net.cloudappi.apigen.generatorcore.config.Configuration;
import net.cloudappi.apigen.generatorcore.generator.ApigenProjectGenerator;
import net.cloudappi.apigen.generatorcore.generator.Project;
import net.cloudappi.apigen.generatorcore.utils.ZipUtils;
import net.cloudappi.apigen.generatorrest.uitls.OpenAPIZipUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/generator")
public class GeneratorController {

	private final ApigenProjectGenerator generator;

	public GeneratorController(ApigenProjectGenerator generator) {
		this.generator = generator;
	}

	@PostMapping(value = "/file", produces = "application/zip")
	public ResponseEntity<Resource> generateFromFile(@RequestParam("file") MultipartFile file) throws IOException {
		Project project;
		if (ZipUtils.isZip(file)) {
			File rootFile = OpenAPIZipUtils.getZipRootFile(file);
			project = generator.generate(rootFile.toPath());
		} else {
			project = generator.generate(file.getBytes());
		}
		return toResponse(project);
	}

	@PostMapping(value = "/config", produces = "application/zip")
	public ResponseEntity<Resource> generateFromConfig(@RequestBody Configuration configuration) throws IOException {
		Project project = generator.generate(configuration);
		return toResponse(project);
	}

	private ResponseEntity<Resource> toResponse(Project project) {
		return new ResponseEntity<>(
				project.getContent(),
				generateHeaderWithAttachment(project.getName()),
				HttpStatus.CREATED
		);
	}

	private HttpHeaders generateHeaderWithAttachment(String fileName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", fileName));
		return headers;
	}
}
