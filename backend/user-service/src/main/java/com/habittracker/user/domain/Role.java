package com.habittracker.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("Обычный пользователь"),
    ADMIN("Администратор");

    private final String description;
}