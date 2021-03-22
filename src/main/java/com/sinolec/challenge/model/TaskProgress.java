package com.sinolec.challenge.model;

public class TaskProgress {
    private GeneralTask task;

    private long progress;

    public GeneralTask getTask() {
        return task;
    }

    public void setTask(GeneralTask task) {
        this.task = task;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }
}
