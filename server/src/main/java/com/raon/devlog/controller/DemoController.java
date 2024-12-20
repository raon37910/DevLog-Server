package com.raon.devlog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raon.devlog.support.response.ApiResponse;

@RestController
public class DemoController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/demo")
	public ApiResponse<String> demo() {
		log.info("info");
		log.debug("debug");
		log.warn("warn");
		log.error("error");
		log.trace("trace");
		return ApiResponse.success("demo");
	}
}
