package de.cluetec.showcase.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

@Configuration
@EnableAutoConfiguration
public class MongoDbConfiguration extends AbstractMongoConfiguration {

	@Autowired
	private MongoProperties properties;

	@Override
	protected String getDatabaseName() {
		return properties.getDatabase();
	}

	@Override
	public Mongo mongo() throws Exception {
		MongoClient client = new MongoClient(properties.getHost(), properties.getPort());
		client.setWriteConcern(WriteConcern.FSYNCED);
		return client;
	}
}
