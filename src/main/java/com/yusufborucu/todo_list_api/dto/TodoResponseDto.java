package com.yusufborucu.todo_list_api.dto;

public record TodoResponseDto(
    Long id,
    String title,
    String description,
    Long userId,
    boolean completed
) {
}
