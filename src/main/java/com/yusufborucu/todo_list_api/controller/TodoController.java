package com.yusufborucu.todo_list_api.controller;

import com.yusufborucu.todo_list_api.dto.TodoDto;
import com.yusufborucu.todo_list_api.dto.TodoResponseDto;
import com.yusufborucu.todo_list_api.model.Todo;
import com.yusufborucu.todo_list_api.service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public TodoResponseDto addTodo(@RequestBody TodoDto todoDto) {
        Todo todo = todoService.addTodo(todoDto);

        return new TodoResponseDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getUser().getId(),
                todo.getCompleted()
        );
    }

    @GetMapping
    public List<TodoResponseDto> getTodos() {
        return todoService.getTodos();
    }

    @GetMapping("/{id}")
    public TodoResponseDto getTodo(@PathVariable Long id) {
        return todoService.getTodo(id);
    }

    @PutMapping("/{id}")
    public TodoResponseDto editTodo(@PathVariable Long id, @RequestBody TodoDto todoDto) {
        Todo todo = todoService.editTodo(id, todoDto);

        return new TodoResponseDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getUser().getId(),
                todo.getCompleted()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }
}
