package com.habittracker.habit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HabitStatus {
    ACTIVE("Активная"),
    ARCHIVED("Заброшенная");

    private final String description;
}