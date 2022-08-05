package de.ast.demo.todo.rest;

import de.ast.demo.todo.persistence.ToDoEntity;
import de.ast.demo.todo.persistence.ToDoRepository;
import de.ast.demo.todo.persistence.ToDoStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ToDoControllerTest {

    @Autowired
    ToDoRepository repository;

    @Autowired
    ToDoController controller;

    @BeforeEach
    void init() {
        repository.deleteAll();
        for (int i = 0; i < 5; i++) {
            repository.save(ToDoEntity.builder().title("Todo " + i).status(ToDoStatus.OPEN).build());
        }
    }

    @Test
    void list() {
        List<ToDo> list = controller.list(Pageable.ofSize(10));
        assertThat(list).hasSize(5);
    }

    @Test
    void create() {
        ToDoCreateUpdate newToDo =  new ToDoCreateUpdate("Create Title",ToDoStatus.OPEN);
        ResponseEntity<ToDo> repsonse = controller.create(newToDo);
        assertThat(repsonse).matches(m -> m.getStatusCode().is2xxSuccessful());
    }

    @ParameterizedTest
    @CsvSource(value = {"0","2","257","300"})
    void createWithInvalidTitleFails(int size) {
        ToDoCreateUpdate newToDo =  new ToDoCreateUpdate(RandomStringUtils.random(size),ToDoStatus.OPEN);
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> controller.create(newToDo));
    }

    @Test
    void createWithInvalidStatusFails() {
        ToDoCreateUpdate newToDo =  new ToDoCreateUpdate(RandomStringUtils.random(100),null);
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> controller.create(newToDo));
    }

    @Test
    void update() {
        ToDoCreateUpdate newToDo =  new ToDoCreateUpdate("Update Title",ToDoStatus.OPEN);
        ResponseEntity<ToDo> response = controller.create(newToDo);
        assertThat(response).matches(m -> m.getStatusCode().is2xxSuccessful());
        assertThat(response.getBody()).isNotNull();
        ToDoCreateUpdate updateToDo =  new ToDoCreateUpdate("Updated and Done Title",ToDoStatus.DONE);
        ResponseEntity updateResponse = controller.update(updateToDo,response.getBody().id());
        assertThat(updateResponse).matches(m -> m.getStatusCode().is2xxSuccessful());
        Optional<ToDoEntity> entity = repository.findById(response.getBody().id());
        assertThat(entity).isPresent();
        assertThat(entity.get()).matches(todo -> todo.getTitle().equals("Updated and Done Title"));
        assertThat(entity.get()).matches(todo -> todo.getStatus().equals(ToDoStatus.DONE));
    }

    @Test
    void StartAndSolveATodoUC(){
        //GIVEN a open Todo
        ToDoEntity todo = repository.save(ToDoEntity.builder().title("My ToDo").status(ToDoStatus.OPEN).build());
        // WHEN the user starts the ToDo
        ResponseEntity starting = controller.update(new ToDoCreateUpdate(todo.getTitle(),ToDoStatus.STARTED),todo.getId());
        // THEN the todo is in status STARTED
        todo = repository.findById(todo.getId()).orElseThrow(() -> new NoSuchElementException("ToDo not found"));
        assertThat(todo).matches(t -> t.getStatus().equals(ToDoStatus.STARTED));
        // WHEN the user then finishes the Todo
        ResponseEntity done = controller.update(new ToDoCreateUpdate(todo.getTitle(),ToDoStatus.DONE),todo.getId());
        // THEN the todo is in status DONE
        todo = repository.findById(todo.getId()).orElseThrow(() -> new NoSuchElementException("ToDo not found"));
        assertThat(todo).matches(t -> t.getStatus().equals(ToDoStatus.DONE));
    }
}