package com.sinolec.challenge.services.task.executors;

import com.sinolec.challenge.model.CounterTask;
import com.sinolec.challenge.model.TaskProgress;
import com.sinolec.challenge.services.task.updaters.TaskUpdaterService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class CounterTaskExecutor implements GeneralTaskExecutor<CounterTask> {

    // just for testing purpose
    private Long timeSourceValue;
    private final TaskUpdaterService taskUpdaterService;

    public CounterTaskExecutor(TaskUpdaterService taskUpdaterService) {
        this.taskUpdaterService = taskUpdaterService;
    }

    public void setTimeSourceValue(Long timeSourceValue) {
        this.timeSourceValue = timeSourceValue;
    }

    @Override
    public void executeTask(CounterTask task) {
        executeTask(task, currentTimeSeconds());
    }

    // startedAt in seconds
    protected void executeTask(CounterTask task, long startedAt) {
        if (task.isCancelled()) {
            throw new IllegalStateException("Cannot execute cancelled task!");
        }
        if (task.isExecuted()) {
            throw new IllegalStateException("Task has been already executed!");
        }
        task.setStartedAt(startedAt);
        task.setExecuted(true);
        taskUpdaterService.update(task.getId(), task);
    }

    @Override
    public void cancelTask(CounterTask task) {
        cancelTask(task, currentTimeSeconds());
        taskUpdaterService.update(task.getId(), task);
    }

    protected void cancelTask(CounterTask task, long cancelledAt) {
        task.setCancelledAt(cancelledAt);
    }

    @Override
    public TaskProgress getTaskProgress(CounterTask task) {
        TaskProgress taskProgress = new TaskProgress();
        taskProgress.setTask(task);
        taskProgress.setProgress(getProgress(task));
        return taskProgress;
    }

    public boolean isTaskFinished(CounterTask task) {
        if (task.isCancelled()) {
            return true;
        }
        return getProgress(task) >= task.getY();
    }

    @Override
    public FileSystemResource getTaskResult(CounterTask task) {
        throw new UnsupportedOperationException("Getting result for that task is not supported!");
    }

    private long getProgress(CounterTask counterTask) {
        final long startedAt = counterTask.getStartedAt();
        final int x = counterTask.getX();
        final int y = counterTask.getY();
        if (startedAt > 0) {
            long cancelledOrCurrentTime = counterTask.isCancelled() ? counterTask.getCancelledAt() : getCurrentTime();
            final long progress = (cancelledOrCurrentTime - startedAt) + x;
            if (progress > y) {
                return y;
            }
            return progress;
        }
        return x;
    }

    long getCurrentTime() {
        if (timeSourceValue != null) {
            return timeSourceValue;
        }
        return currentTimeSeconds();
    }

    private static long currentTimeSeconds() {
        return (System.currentTimeMillis() / 1000);
    }
}
