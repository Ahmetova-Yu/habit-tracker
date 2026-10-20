package com.habittracker.habit.web.dto;

import com.habittracker.habit.domain.HabitFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHabitRequest {
    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private HabitFrequency frequency;

    @Size(max = 16)
    private String icon;
}