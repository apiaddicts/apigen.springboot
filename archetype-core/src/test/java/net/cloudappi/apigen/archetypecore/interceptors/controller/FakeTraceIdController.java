package net.cloudappi.apigen.archetypecore.interceptors.controller;

import lombok.extern.slf4j.Slf4j;
import net.cloudappi.apigen.archetypecore.interceptors.ApigenContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/trace")
public class FakeTraceIdController {

	@GetMapping
	public String trace() {
		return ApigenContext.getTraceId();
	}

}
