package de.cluetec.showcase.test.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.test.web.servlet.MvcResult;

import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.model.TodoItem.Status;

public class TodoControllerTest extends AbstractRestControllerTest {

	@Test
	public void testThatPostingTodoItemsReturnsCreatedStatusAndTheNewTodoItem() throws HttpMessageNotWritableException,
			IOException, Exception {
		TodoItem item = new TodoItem();
		item.setTitle("Test the REST");
		item.setDueDate(Date.from(Instant.parse("2015-06-14T12:30:00.00Z")));
		item.setStatus(Status.TODO);
		item.addTag("spring");
		item.addTag("rest");
		item.addTag("java");

		mockMvc.perform(post("/api/todos")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isCreated())//
				.andExpect(jsonPath("$.id", notNullValue()))//
				.andExpect(jsonPath("$.createdDate", notNullValue()))//
				.andExpect(jsonPath("$.title", equalTo(item.getTitle())))//
				.andExpect(jsonPath("$.status", equalTo(Status.TODO.name())))//
				.andExpect(jsonPath("$.tags", hasSize(3)))//
				.andExpect(jsonPath("$.tags", hasItems("spring", "java", "rest")));
	}

	@Test
	public void testThatGetAllTodoItemsReturnsSingleEmptyPageWhenDatabaseIsEmpty() throws Exception {
		mockMvc.perform(get("/api/todos"))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("$.numberOfElements", equalTo(0)))//
				.andExpect(jsonPath("$.totalElements", equalTo(0)))//
				.andExpect(jsonPath("$.content", hasSize(0)));
	}

	@Test
	public void testThatGetAllTodoItemsReturnsSinglePageWithSingleItemAfterInitialPost() throws Exception {
		TodoItem item = new TodoItem();
		item.setTitle("Test the REST");
		item.setDueDate(Date.from(Instant.parse("2015-06-14T12:30:00.00Z")));
		item.setStatus(Status.TODO);
		item.addTag("spring");
		item.addTag("rest");
		item.addTag("java");

		mockMvc.perform(post("/api/todos")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/todos"))//
				.andExpect(status().isOk())//
				.andExpect(jsonPath("$.numberOfElements", equalTo(1)))//
				.andExpect(jsonPath("$.totalElements", equalTo(1)))//
				.andExpect(jsonPath("$.content", hasSize(1)))//
				.andExpect(jsonPath("$.content.[0].title", equalTo("Test the REST")));
	}

	@Test
	public void testThatModifyingCreationDateReturnsConflict() throws HttpMessageNotWritableException, IOException,
			Exception {
		TodoItem item = new TodoItem();
		item.setTitle("Test the REST");
		item.setDueDate(Date.from(Instant.parse("2015-06-14T12:30:00.00Z")));
		item.setStatus(Status.TODO);
		item.addTag("spring");
		item.addTag("rest");
		item.addTag("java");

		MvcResult postResult = mockMvc.perform(post("/api/todos")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isCreated())//
				.andReturn();

		byte[] jsonStringAsBytes = postResult.getResponse().getContentAsByteArray();
		item = entity(jsonStringAsBytes, TodoItem.class);

		item.setCreatedDate(new Date());
		mockMvc.perform(put("/api/todos/" + item.getId().toHexString())//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json(item)))//
				.andExpect(status().isConflict());
	}

}
