package com.sinolec.challenge.services.task.updaters;

import com.sinolec.challenge.model.GeneralTask;

public interface GeneralTaskUpdater<T extends GeneralTask> {
    /**
     *
     * @param currentTask existing task
     * @param updatedTask updated version
     * @return
     */
    T update(T currentTask, T updatedTask);
}
