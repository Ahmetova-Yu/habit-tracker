package com.habittracker.habit.web;

import com.habittracker.common.security.SecurityConstants;
import com.habittracker.habit.service.HabitService;
import com.habittracker.habit.web.dto.CreateHabitRequest;
import com.habittracker.habit.web.dto.HabitDto;
import com.habittracker.habit.web.dto.UpdateHabitRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitDto create(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @Valid @RequestBody CreateHabitRequest request) {
        return habitService.create(userId, request);
    }

    @GetMapping
    public List<HabitDto> getActive(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId) {
        return habitService.getActive(userId);
    }

    @GetMapping("/archived")
    public List<HabitDto> getArchived(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId) {
        return habitService.getArchived(userId);
    }

    @PutMapping("/{id}")
    public HabitDto update(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateHabitRequest request) {
        return habitService.update(userId, id, request);
    }

    @PatchMapping("/{id}/archive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void archive(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @PathVariable Long id) {
        habitService.archive(userId, id);
    }

    @PatchMapping("/{id}/restore")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restore(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @PathVariable Long id) {
        habitService.restore(userId, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId,
            @PathVariable Long id) {
        habitService.delete(userId, id);
    }
}