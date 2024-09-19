package com.yusufborucu.todo_list_api.dto;

public record TodoDto (
    String title,
    String description,
    boolean completed
) {
}
