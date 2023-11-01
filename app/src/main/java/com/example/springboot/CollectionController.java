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
	double collectionSize = Math.pow(2, 20);
	Random random = new Random();

	@GetMapping("/collection/create")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void create() {
		collection.clear();
		for (double n = collectionSize; n >= 0; n--) {
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
	public void drain(@RequestParam(defaultValue = "5") Integer percentage) {
		int elementsToDrain = (int) random.nextDouble(collectionSize * (percentage / 100.0));
		for (int i = elementsToDrain; i > 0; i--) {
			collection.remove(i);
		}
	}

}
