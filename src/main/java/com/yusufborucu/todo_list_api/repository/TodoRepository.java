package com.yusufborucu.todo_list_api.repository;

import com.yusufborucu.todo_list_api.dto.TodoResponseDto;
import com.yusufborucu.todo_list_api.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<TodoResponseDto> findByUserId(Long userId);
    Optional<Todo> findByIdAndUserId(Long id, Long userId);
}
