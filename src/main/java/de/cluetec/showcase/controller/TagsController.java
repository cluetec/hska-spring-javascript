package de.cluetec.showcase.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.cluetec.showcase.service.TodoService;

@RestController
@RequestMapping("/tags")
public class TagsController {

	private TodoService todoService;

	@Autowired
	public TagsController(TodoService todoService) {
		this.todoService = todoService;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> getAllTags() {
		Set<String> allTags = todoService.getAllTags();
		Map<String, Object> map = new HashMap<>();
		map.put("tags", allTags);
		map.put("totalNumber", allTags.size());
		return map;
	}
}
