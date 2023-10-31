package com.example.springboot;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiscController {

	@GetMapping("/singularity")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void boom() {
		System.out.println(5 / 0);
	}
}
