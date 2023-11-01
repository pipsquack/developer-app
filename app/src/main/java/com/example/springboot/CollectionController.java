package com.example.springboot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {

	List<String> collection = new ArrayList<String>();
	Random random = new Random();

	@GetMapping("/collection/create")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void create() {
		collection.clear();
		for (int n = 2 ^ 24; n >= 0; n--) {
			collection.add(String.valueOf(new Random().nextInt(200)));
		}
	}

	@GetMapping("/collection/shuffle")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void shuffle() {
		Collections.shuffle(collection);
	}

	@GetMapping("/collection/sort")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void sort() {
		Collections.sort(collection);
	}

	@GetMapping("/collection/drain")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void drain(@RequestParam(defaultValue = "50") Integer upperBound) {
		for (int i = random.nextInt(upperBound); i > 0; i--) {
			collection.remove(i);
		}
	}

}
