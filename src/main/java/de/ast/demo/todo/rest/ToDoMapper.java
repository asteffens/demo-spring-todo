package de.ast.demo.todo.rest;

import de.ast.demo.todo.persistence.ToDoEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class ToDoMapper {

    public abstract ToDo dto(ToDoEntity ent);

    public abstract  ToDoEntity entity(ToDoCreateUpdate dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract  ToDoEntity update(ToDoCreateUpdate dto, @MappingTarget ToDoEntity entity);

}
