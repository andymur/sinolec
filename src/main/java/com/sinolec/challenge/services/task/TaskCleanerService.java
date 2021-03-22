package com.sinolec.challenge.services.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TaskCleanerService {

    private TaskCleaner taskCleaner;

    public TaskCleanerService(TaskCleaner taskCleaner) {
        this.taskCleaner = taskCleaner;
    }

    @Scheduled(fixedRateString="${taskService.cleaningIntervalInMs}")
    public void launchCleaning() {
        taskCleaner.clean();
    }
}
