package com.yusufborucu.todo_list_api.dto;

import com.yusufborucu.todo_list_api.model.User;

public record TodoDto (
    String title,
    String description,
    User user,
    boolean completed
) {
}
