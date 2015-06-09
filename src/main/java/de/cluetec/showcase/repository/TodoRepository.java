package de.cluetec.showcase.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.model.TodoItem.Status;

public interface TodoRepository extends MongoRepository<TodoItem, ObjectId> {

	List<TodoItem> findByStatus(Status status);
	List<TodoItem> findByDueDateAfter(Date date);
	List<TodoItem> findByTagsInIgnoreCase(Set<String> tags);

	/* For demo purposes */
	List<TodoItem> findByCreatedDateAfter(Date date);

	List<TodoItem> findByCreatedDateBetween(Date from, Date to);

	Page<TodoItem> findByStatus(Status status, Pageable pageable);

	List<TodoItem> findFirst10ByStatusOrderByDueDateAsc(Status status, Date date);

	Stream<TodoItem> streamAllPaged(Pageable pageable);

	@Query("{ 'status': 'TODO', 'createdDate': {'$lte': new Date('2014-01-01T00:00:00.000Z')} }")
	List<TodoItem> findOldTodos();
}
