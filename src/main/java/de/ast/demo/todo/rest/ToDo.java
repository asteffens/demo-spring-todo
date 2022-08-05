package de.ast.demo.todo.rest;

import de.ast.demo.todo.persistence.ToDoStatus;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public record ToDo(@NotNull String id, @NotNull @Size(max = 256) String title, @NotNull ToDoStatus status,
                   @NotNull LocalDateTime created, @NotNull LocalDateTime modified) {

}
