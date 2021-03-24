package com.sinolec.challenge.controllers;

import java.util.Collections;
import java.util.List;

import org.asynchttpclient.AsyncHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request")
public class AsyncClientController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncClientController.class);
	private final AsyncHttpClient httpClient;

	public AsyncClientController(final AsyncHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@GetMapping("/")
	public List<String> requestApi() {
		LOGGER.info("requestApi.start; client={}", httpClient);
		return Collections.emptyList();
	}
}
