package com.habittracker.habit.scheduler;

import com.habittracker.habit.domain.Habit;
import com.habittracker.habit.domain.HabitStatus;
import com.habittracker.habit.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HabitScheduler {

    private final HabitRepository habitRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void archiveInactiveHabits() {
        log.info("Starting daily check for inactive habits...");

        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        List<Habit> inactiveHabits = habitRepository.findInactiveHabits(thirtyDaysAgo);

        for (Habit habit : inactiveHabits) {
            habit.setStatus(HabitStatus.ARCHIVED);
            habit.setArchivedAt(Instant.now());
            log.info("Habit archived due to 30 days inactivity: id={}", habit.getId());
        }

        habitRepository.saveAll(inactiveHabits);
        log.info("Archived {} inactive habits", inactiveHabits.size());
    }
}