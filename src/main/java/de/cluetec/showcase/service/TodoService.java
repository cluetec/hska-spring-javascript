package de.cluetec.showcase.service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.cluetec.showcase.exceptions.TodoAppIllegalActionException;
import de.cluetec.showcase.exceptions.TodoItemNotFoundException;
import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.repository.TodoRepository;

@Service
public class TodoService {

	@Autowired
	private TodoRepository todoRepository;

	public Page<TodoItem> getAllTodoItems(Pageable pageable) {
		Page<TodoItem> page = todoRepository.findAll(pageable);
		return page;
	}

	public List<TodoItem> getTodoItemsByTags(Set<String> tags) {
		List<TodoItem> list = todoRepository.findByTagsIn(tags);
		return list;
	}

	public TodoItem getTodoItemsById(String itemId) {
		TodoItem item = todoRepository.findOne(new ObjectId(itemId));
		if (item == null) {
			throw new TodoItemNotFoundException("Could not find item for the given ID " + itemId);
		}
		return item;
	}

	public TodoItem createTodoItem(TodoItem item) {
		if (item.getCreatedDate() == null) {
			item.setCreatedDate(new Date());
		}
		return todoRepository.save(item);
	}

	public TodoItem modifyTodoItem(String itemId, TodoItem item) {
		TodoItem existing = todoRepository.findOne(new ObjectId(itemId));
		if (existing == null) {
			throw new TodoItemNotFoundException("Could not find item for the given ID " + itemId);
		}

		if (!item.getCreatedDate().equals(existing.getCreatedDate())) {
			throw new TodoAppIllegalActionException("Cannot modify the creation date");
		}
		return todoRepository.save(item);
	}

	public void deleteTodoItem(String itemId) {
		todoRepository.delete(new ObjectId(itemId));
	}

	public Set<String> getAllTags() {
		List<TodoItem> allItems = todoRepository.findAll();
		Set<String> allTags = allItems.stream()//
				.flatMap(item -> item.getTags().stream())//
				.collect(Collectors.toSet());
		return allTags;
	}
}
