package de.cluetec.showcase.utils;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
	@Override
	public ObjectId deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException,
			JsonProcessingException {
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode node = codec.readTree(jsonParser);
		return new ObjectId(node.textValue());
	}
}