package com.habittracker.habit.repository;

import com.habittracker.habit.domain.Habit;
import com.habittracker.habit.domain.HabitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findAllByUserIdAndStatusOrderByCreatedAtDesc(Long userId, HabitStatus status);

    Optional<Habit> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT h FROM Habit h WHERE h.status = 'ACTIVE' AND h.lastCompletedAt < :threshold")
    List<Habit> findInactiveHabits(@Param("threshold") Instant threshold);
}