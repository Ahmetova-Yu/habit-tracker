package com.habittracker.habit.web.dto;

import com.habittracker.habit.domain.HabitFrequency;
import com.habittracker.habit.domain.HabitStatus;
import lombok.Builder;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;

@Value
@Builder
public class HabitDto {
    Long id;
    String name;
    String description;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    HabitFrequency frequency;
    String icon;
    Integer currentStreak;
    Integer bestStreak;
    HabitStatus status;
    Instant createdAt;
}