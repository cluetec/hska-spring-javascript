package de.cluetec.showcase.test.rest;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.cluetec.showcase.test.AbstractTodoAppTest;

public abstract class AbstractRestControllerTest extends AbstractTodoAppTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private HttpMessageConverter<Object> messageConverter;

	protected MockMvc mockMvc;

	@Before
	public void setupMockMvc() throws UnknownHostException {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	public String json(Object object) throws HttpMessageNotWritableException, IOException {
		MockHttpOutputMessage message = new MockHttpOutputMessage();
		messageConverter.write(object, MediaType.APPLICATION_JSON, message);
		return message.getBodyAsString();
	}

	public <T> T entity(byte[] contents, Class<T> clazz) throws Exception {
		MockHttpInputMessage message = new MockHttpInputMessage(contents);
		Object read = messageConverter.read(clazz, message);
		return clazz.cast(read);
	}
}
