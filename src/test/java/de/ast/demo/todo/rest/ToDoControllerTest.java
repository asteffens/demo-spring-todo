package de.ast.demo.todo.rest;

import de.ast.demo.todo.persistence.ToDoEntity;
import de.ast.demo.todo.persistence.ToDoRepository;
import de.ast.demo.todo.persistence.ToDoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Page<ToDoEntity> list = repository.findAll(Pageable.ofSize(10));
        assertThat(list).hasSize(5);
    }

    @Test
    void create() {
        ToDoCreateUpdate newToDo =  new ToDoCreateUpdate("Create Title",ToDoStatus.OPEN);
        ResponseEntity repsonse = controller.create(newToDo);
        assertThat(repsonse).matches(m -> m.getStatusCode().is2xxSuccessful());
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
}