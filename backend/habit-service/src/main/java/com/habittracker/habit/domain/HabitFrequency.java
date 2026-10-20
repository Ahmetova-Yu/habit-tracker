package com.habittracker.habit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HabitFrequency {
    DAILY("Ежедневно"),
    WEEKLY("Еженедельно"),
    CUSTOM("Своя частота");

    private final String description;
}