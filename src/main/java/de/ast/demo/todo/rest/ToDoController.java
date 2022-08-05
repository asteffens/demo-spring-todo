package de.ast.demo.todo.rest;

import de.ast.demo.todo.persistence.ToDoEntity;
import de.ast.demo.todo.persistence.ToDoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController("/todo")
@Validated
@Slf4j
public class ToDoController {

    private final ToDoRepository repository;
    private final ToDoMapper mapper;

    public ToDoController(ToDoRepository repository, ToDoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all ToDos",
            description = "Get a list of all ToDos and their Status")
    @GetMapping("/")
    public List<ToDo> list(@SortDefault.SortDefaults({@SortDefault(sort = "created", direction = Sort.Direction.DESC)}) Pageable pageable) {
        return repository.findAll(pageable).stream().map(mapper::dto).collect(Collectors.toList());
    }

    @Operation(summary = "Create a new ToDo",
            description = "Create a new ToDo with title and status, input values are validated")
    @PostMapping("/")
    public ResponseEntity<ToDo> create(@Valid @RequestBody ToDoCreateUpdate td) {
        ToDoEntity entity = repository.save(mapper.entity(td));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.dto(entity));
    }

    @Operation(summary = "Update an existing ToDo",
            description = "Update an existing ToDo with new title and/or status, input values are validated",
            parameters = {@Parameter(name = "id",description = "The id of the ToDo to be updated")})
    @PutMapping("/{id}")
    public ResponseEntity update(@Valid @RequestBody ToDoCreateUpdate td, @PathVariable String id) {
        ToDoEntity entity = repository.findById(id).orElseThrow(() -> new NoSuchElementException("ToDo not found"));
        repository.save(mapper.update(td, entity));
        return ResponseEntity.ok().build();
    }

}
