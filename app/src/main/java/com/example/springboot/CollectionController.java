package com.example.springboot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {

	List<String> collection = new ArrayList<String>();
	double collectionSize = Math.pow(2, 20);
	Random random = new Random();

	@PutMapping("/collection/create")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void create() {
		synchronized (collection) {
			collection.clear();
			for (double n = collectionSize; n >= 0; n--) {
				collection.add(String.valueOf(new Random().nextInt(200)));
			}
		}
	}

	@PatchMapping("/collection/shuffle")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void shuffle() {
		synchronized (collection) {
			Collections.shuffle(collection, random);
		}
	}

	@PatchMapping("/collection/sort")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void sort() {
		synchronized (collection) {
			Collections.sort(collection);
		}
	}

	@DeleteMapping("/collection/drain")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void drain(@RequestParam(defaultValue = "5") Integer percentage) {
		int elementsToDrain = (int) random.nextDouble(collectionSize * (percentage / 100.0));
		synchronized (collection) {
			for (int i = elementsToDrain; i > 0; i--) {
				collection.remove(i);
			}
		}
	}

	@PutMapping("/collection/add/{element}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void addElement(@PathVariable String element) {
		synchronized (collection) {
			collection.add(element);
		}
	}

}
