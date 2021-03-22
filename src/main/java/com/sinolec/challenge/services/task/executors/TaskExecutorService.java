package com.sinolec.challenge.services.task.executors;

import com.sinolec.challenge.model.GeneralTask;
import com.sinolec.challenge.model.TaskProgress;
import com.sinolec.challenge.model.TaskType;
import com.sinolec.challenge.services.task.TaskRetrieverService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
public class TaskExecutorService {

    private final TaskRetrieverService taskRetrieverService;

    private final ProjectGenerationTaskExecutor projectGenerationTaskExecutor;
    private final CounterTaskExecutor counterTaskExecutor;

    public TaskExecutorService(TaskRetrieverService taskRetrieverService,
                               ProjectGenerationTaskExecutor projectGenerationTaskExecutor,
                               CounterTaskExecutor counterTaskExecutor) {
        this.taskRetrieverService = taskRetrieverService;
        this.projectGenerationTaskExecutor = projectGenerationTaskExecutor;
        this.counterTaskExecutor = counterTaskExecutor;
    }

    public void executeTask(String taskId) {
        final GeneralTask task = taskRetrieverService.getTask(taskId);
        chooseExecutor(task.getType()).executeTask(task);
    }

    public FileSystemResource getTaskResult(String taskId) {
        final GeneralTask task = taskRetrieverService.getTask(taskId);
        return chooseExecutor(task.getType()).getTaskResult(task);
    }

    public TaskProgress getTaskProgress(String taskId) {
        final GeneralTask task = taskRetrieverService.getTask(taskId);
        return chooseExecutor(task.getType()).getTaskProgress(task);
    }

    public void cancelTask(String taskId) {
        final GeneralTask task = taskRetrieverService.getTask(taskId);
        chooseExecutor(task.getType()).cancelTask(task);
    }

    @SuppressWarnings("unchecked")
    private <T extends GeneralTask> GeneralTaskExecutor<T> chooseExecutor(TaskType taskType) {
        switch (taskType) {
            case PROJECT_GENERATION_TASK:
                return (GeneralTaskExecutor<T>) projectGenerationTaskExecutor;
            case COUNTER_TASK:
                return (GeneralTaskExecutor<T>) counterTaskExecutor;
        }
        throw new IllegalArgumentException("Unknown task with a type of: " + taskType);
    }
}
