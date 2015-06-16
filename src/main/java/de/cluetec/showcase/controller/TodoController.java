package de.cluetec.showcase.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.cluetec.showcase.exceptions.TodoAppInvalidArgumentException;
import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	private TodoService todoService;

	@Autowired
	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public Page<TodoItem> getAllTodoItemsFiltered(//
			@RequestParam(required = false) String title,//
			@RequestParam(required = false) Set<String> tags,//
			Pageable pageable) {
		Page<TodoItem> page = todoService.getTodoItemsFiltered(title, tags, pageable);
		return page;
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public TodoItem getTodoItemById(@PathVariable String itemId) {
		TodoItem item = todoService.getTodoItemsById(itemId);
		return item;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public TodoItem createTodoItem(@RequestBody TodoItem item) {
		item = todoService.createTodoItem(item);
		return item;
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void modifyTodoItem(@PathVariable String itemId, @RequestBody TodoItem item) {
		if (!itemId.equals(item.getId().toHexString())) {
			throw new TodoAppInvalidArgumentException("Item IDs in URL and Payload do not match");
		}
		todoService.modifyTodoItem(itemId, item);
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteTodoItem(@PathVariable String itemId) {
		todoService.deleteTodoItem(itemId);
	}
}
