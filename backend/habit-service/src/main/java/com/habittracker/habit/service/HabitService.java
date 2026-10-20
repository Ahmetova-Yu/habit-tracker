package com.habittracker.habit.service;

import com.habittracker.common.exception.NotFoundException;
import com.habittracker.habit.domain.Habit;
import com.habittracker.habit.domain.HabitStatus;
import com.habittracker.habit.repository.HabitRepository;
import com.habittracker.habit.web.dto.CreateHabitRequest;
import com.habittracker.habit.web.dto.HabitDto;
import com.habittracker.habit.web.dto.UpdateHabitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;

    @Transactional
    public HabitDto create(Long userId, CreateHabitRequest request) {
        Habit habit = Habit.builder()
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .frequency(request.getFrequency())
                .icon(request.getIcon())
                .build();

        Habit saved = habitRepository.save(habit);
        log.info("Habit created: id={}, userId={}", saved.getId(), userId);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<HabitDto> getActive(Long userId) {
        return habitRepository
                .findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, HabitStatus.ACTIVE)
                .stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<HabitDto> getArchived(Long userId) {
        return habitRepository
                .findAllByUserIdAndStatusOrderByCreatedAtDesc(userId, HabitStatus.ARCHIVED)
                .stream().map(this::toDto).toList();
    }

    @Transactional
    public HabitDto update(Long userId, Long habitId, UpdateHabitRequest request) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new NotFoundException("Habit not found"));

        if (request.getName() != null) habit.setName(request.getName());
        if (request.getDescription() != null) habit.setDescription(request.getDescription());
        if (request.getDayOfWeek() != null) habit.setDayOfWeek(request.getDayOfWeek());
        if (request.getStartTime() != null) habit.setStartTime(request.getStartTime());
        if (request.getFrequency() != null) habit.setFrequency(request.getFrequency());
        if (request.getIcon() != null) habit.setIcon(request.getIcon());

        return toDto(habit);
    }

    @Transactional
    public void archive(Long userId, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new NotFoundException("Habit not found"));
        habit.setStatus(HabitStatus.ARCHIVED);
        habit.setArchivedAt(Instant.now());
    }

    @Transactional
    public void restore(Long userId, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new NotFoundException("Habit not found"));
        habit.setStatus(HabitStatus.ACTIVE);
        habit.setArchivedAt(null);
    }

    @Transactional
    public void delete(Long userId, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new NotFoundException("Habit not found"));
        habitRepository.delete(habit);
        log.info("Habit hard deleted: id={}", habitId);
    }

    private HabitDto toDto(Habit habit) {
        return HabitDto.builder()
                .id(habit.getId())
                .name(habit.getName())
                .description(habit.getDescription())
                .dayOfWeek(habit.getDayOfWeek())
                .startTime(habit.getStartTime())
                .frequency(habit.getFrequency())
                .icon(habit.getIcon())
                .currentStreak(habit.getCurrentStreak())
                .bestStreak(habit.getBestStreak())
                .status(habit.getStatus())
                .createdAt(habit.getCreatedAt())
                .build();
    }
}