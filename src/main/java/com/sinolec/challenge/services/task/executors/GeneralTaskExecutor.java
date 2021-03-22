package com.sinolec.challenge.services.task.executors;

import com.sinolec.challenge.model.GeneralTask;
import com.sinolec.challenge.model.TaskProgress;
import org.springframework.core.io.FileSystemResource;

public interface GeneralTaskExecutor<T extends GeneralTask> {
    /**
     *
     * @param task going to be executed
     */
    void executeTask(T task);

    /**
     *
     * @param task going to be cancelled (if possible) or raise an exception
     */
    void cancelTask(T task);

    /**
     *
     * @param task which progress will be returned
     * @return task progress (if possible) as a counter (but would be better in per cent [0..100]) or raise an exception
     */
    TaskProgress getTaskProgress(T task);

    /**
     *
     * @param task which result will be returned
     * @return task result (as a file and if possible) or raise an exception
     */
    FileSystemResource getTaskResult(T task);

}
