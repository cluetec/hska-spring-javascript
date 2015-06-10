package de.cluetec.showcase.test.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.model.TodoItem.Status;
import de.cluetec.showcase.repository.TodoRepository;
import de.cluetec.showcase.test.AbstractTodoAppTests;

/**
 * These tests are for demo purposes. 
 */
public class TodoRepositoryTests extends AbstractTodoAppTests {

	@Autowired
	private TodoRepository todoRepository;

	@Test
	public void testThatRepositorySavesTodoItems() {
		for (int i = 0; i < 100; i++) {
			TodoItem item = new TodoItem();
			item.setTitle("Todo " + i);
			item.setStatus(Status.TODO);
			item.setCreatedDate(new Date());
			item.setDueDate(new Date(item.getCreatedDate().getTime() + 3600));
			todoRepository.save(item);
		}

		assertThat(todoRepository.count(), equalTo(100));
	}

	@Test
	public void testThatRepositoryFindsTodoItemsByTags() {
		TodoItem item1 = new TodoItem();
		item1.setTitle("Todo 1");
		item1.setCreatedDate(new Date());
		item1.setDueDate(new Date(item1.getCreatedDate().getTime() + 3600));
		item1.addTag("tag1");
		todoRepository.save(item1);

		TodoItem item2 = new TodoItem();
		item2.setTitle("Todo 2");
		item2.setCreatedDate(new Date());
		item2.setDueDate(new Date(item2.getCreatedDate().getTime() + 3600));
		item2.addTag("tag1");
		item2.addTag("tag2");
		todoRepository.save(item2);

		Set<String> tags = new HashSet<>();
		tags.add("tag1");

		assertThat(todoRepository.findByTagsIn(tags), containsInAnyOrder(item1, item2));

		tags.clear();
		tags.add("tag2");
		assertThat(todoRepository.findByTagsIn(tags), containsInAnyOrder(item2));

		tags.add("tag1");
		assertThat(todoRepository.findByTagsIn(tags), containsInAnyOrder(item1, item2));
	}
}
