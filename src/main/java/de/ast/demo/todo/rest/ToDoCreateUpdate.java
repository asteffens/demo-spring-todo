package de.ast.demo.todo.rest;

import de.ast.demo.todo.persistence.ToDoStatus;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ToDoCreateUpdate(@NotNull @Size(min=3,max = 256) String title, @NotNull ToDoStatus status) {

}
