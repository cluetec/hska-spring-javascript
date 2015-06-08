package de.cluetec.showcase.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import de.cluetec.showcase.model.TodoItem;

public interface TodoRepository extends MongoRepository<TodoItem, ObjectId> {

}
