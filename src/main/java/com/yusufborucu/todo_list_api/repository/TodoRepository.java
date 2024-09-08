package com.yusufborucu.todo_list_api.repository;

import com.yusufborucu.todo_list_api.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
