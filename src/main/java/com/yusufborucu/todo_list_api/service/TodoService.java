package com.yusufborucu.todo_list_api.service;

import com.yusufborucu.todo_list_api.dto.TodoDto;
import com.yusufborucu.todo_list_api.dto.TodoResponseDto;
import com.yusufborucu.todo_list_api.exception.TodoNotFoundException;
import com.yusufborucu.todo_list_api.model.Todo;
import com.yusufborucu.todo_list_api.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;

    public TodoService(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    public Todo addTodo(TodoDto todoDto) {
        Todo todo = new Todo();
        todo.setTitle(todoDto.title());
        todo.setDescription(todoDto.description());
        todo.setUser(userService.getAuthenticatedUser());
        return todoRepository.save(todo);
    }

    public List<TodoResponseDto> getTodos() {
        return todoRepository.findByUserId(userService.getAuthenticatedUser().getId());
    }

    public TodoResponseDto getTodo(Long id) {
        Optional<Todo> todo = todoRepository.findByIdAndUserId(id, userService.getAuthenticatedUser().getId());
        if (todo.isPresent()) {
            return new TodoResponseDto(
                    todo.get().getId(),
                    todo.get().getTitle(),
                    todo.get().getDescription(),
                    todo.get().getUser().getId(),
                    todo.get().getCompleted()
            );
        }
        throw new TodoNotFoundException("Todo not found");
    }

    public Todo editTodo(Long id, TodoDto todoDto) {
        Optional<Todo> existTodo = todoRepository.findByIdAndUserId(id, userService.getAuthenticatedUser().getId());
        if (existTodo.isPresent()) {
            existTodo.get().setTitle(todoDto.title());
            existTodo.get().setDescription(todoDto.description());
            existTodo.get().setCompleted(todoDto.completed());
            return todoRepository.save(existTodo.get());
        }
        throw new TodoNotFoundException("Todo not found");
    }

    public void deleteTodo(Long id) {
        Optional<Todo> existTodo = todoRepository.findByIdAndUserId(id, userService.getAuthenticatedUser().getId());
        if (existTodo.isPresent()) {
            todoRepository.deleteById(id);
        }
    }
}
