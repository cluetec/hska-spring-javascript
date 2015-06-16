package de.cluetec.showcase.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import de.cluetec.showcase.model.TodoItem;
import de.cluetec.showcase.model.TodoItem.Status;

public interface TodoRepository extends MongoRepository<TodoItem, ObjectId> {

	List<TodoItem> findByStatus(Status status);

	List<TodoItem> findByCreatedDateBetween(Date from, Date to);

	@Query("{ 'status': 'TODO', 'createdDate': {'$lte': {'$date': '2014-01-01T00:00:00.000Z'} } }")
	List<TodoItem> findOldTodos();

	List<TodoItem> findByDueDateAfter(Date date);

	List<TodoItem> findByTagsIn(Set<String> tags);

	Page<TodoItem> findByTitleRegex(String regex, Pageable pageable);

	Page<TodoItem> findByTitleRegexAndTagsIn(String titleRegex, Set<String> tags, Pageable pageable);

	List<TodoItem> findByCreatedDateAfter(Date date);

	Page<TodoItem> findByStatus(Status status, Pageable pageable);

	List<TodoItem> findFirst10ByStatusOrderByDueDateAsc(Status status, Date date);
}
