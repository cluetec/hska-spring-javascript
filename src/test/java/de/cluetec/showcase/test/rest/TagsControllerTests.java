package de.cluetec.showcase.test.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.model.TodoItem.Status;

public class TagsControllerTests extends AbstractRestControllerTests {

	@Test
	public void testThatGetAllTagsReturnAllTagsAggregatedOverAllTodoItems() throws Exception {
		TodoItem item = new TodoItem();
		item.setTitle("Test the REST");
		item.setDueDate(Date.from(Instant.parse("2015-06-14T12:30:00.00Z")));
		item.setStatus(Status.TODO);
		item.addTag("spring");

		mockMvc.perform(post("/api/todos")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isCreated());

		item.addTag("rest");

		mockMvc.perform(post("/api/todos")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isCreated());

		item.addTag("java");

		mockMvc.perform(post("/api/todos")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/tags"))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("$.totalNumber", equalTo(3)))//
				.andExpect(jsonPath("$.tags", hasItems("spring", "java", "rest")));
	}

	@Test
	public void testThatCreatingNewTagsDirectlyIsNotAllowed() throws HttpMessageNotWritableException, IOException,
			Exception {

		mockMvc.perform(post("/api/tags")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content("new_tag"))//
				.andExpect(status().isMethodNotAllowed());
	}
}
