package de.ast.demo.todo.persistence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Document
public class ToDoEntity {

    @Id
    String id = UUID.randomUUID().toString();

    @Size(max = 256)
    @NotNull
    @Indexed(unique = true)
    String title;

    @NotNull
    ToDoStatus status;

    @CreatedDate
    @NotNull
    LocalDateTime created;

    @LastModifiedDate
    @NotNull
    LocalDateTime modified;

}
