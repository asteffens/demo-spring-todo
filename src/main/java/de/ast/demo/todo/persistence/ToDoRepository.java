package de.ast.demo.todo.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ToDoRepository extends PagingAndSortingRepository<ToDoEntity, String> {
}
