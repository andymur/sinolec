package com.sinolec.challenge.services.task;

import com.sinolec.challenge.model.GeneralTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TaskCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCleaner.class);
    private final GeneralTaskRepository generalTaskRepository;
    private final int days;

    public TaskCleaner(GeneralTaskRepository generalTaskRepository,
                       @Value("${taskService.cleaningOlderThanDays}") int days) {
        this.generalTaskRepository = generalTaskRepository;
        this.days = days;
    }

    @Transactional
    public void clean() {
        final Date nDaysAgoStamp = nDaysAgo(days);
        LOGGER.info("clean.started(); cleaning non executed tasks older than: {}", nDaysAgoStamp);
        final int deletedTasksNum = generalTaskRepository.deleteNonExecutedTasksOlderThanProvidedDate(nDaysAgoStamp);
        LOGGER.info("clean.finished(); number of cleaned tasks: {}", deletedTasksNum);
    }

    Date nDaysAgo(int days) {
        LocalDateTime backThenLdt = LocalDateTime.now().minusDays(days);
        return Date.from(backThenLdt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
