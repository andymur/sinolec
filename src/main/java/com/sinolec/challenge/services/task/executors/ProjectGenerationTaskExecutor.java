package com.sinolec.challenge.services.task.executors;

import com.sinolec.challenge.exceptions.InternalException;
import com.sinolec.challenge.model.ProjectGenerationTask;
import com.sinolec.challenge.model.TaskProgress;
import com.sinolec.challenge.services.FileService;
import com.sinolec.challenge.services.task.updaters.TaskUpdaterService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class ProjectGenerationTaskExecutor implements GeneralTaskExecutor<ProjectGenerationTask> {

    private final FileService fileService;
    private final TaskUpdaterService taskUpdaterService;

    public ProjectGenerationTaskExecutor(TaskUpdaterService taskUpdaterService,
                                         FileService fileService) {
        this.fileService = fileService;
        this.taskUpdaterService = taskUpdaterService;
    }

    @Override
    public void executeTask(ProjectGenerationTask task) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("challenge.zip");
        if (url == null) {
            throw new InternalException("Zip file not found");
        }
        try {
            final String storageLocation = fileService.storeResult(task.getId(), url);
            task.setStorageLocation(storageLocation);
            task.setExecuted(true);
            taskUpdaterService.update(task.getId(), task);
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    @Override
    public void cancelTask(ProjectGenerationTask task) {
        throw new UnsupportedOperationException("Cannot be cancelled!");
    }

    @Override
    public TaskProgress getTaskProgress(ProjectGenerationTask task) {
        throw new UnsupportedOperationException("Measuring progress for that task is not supported!");
    }

    public FileSystemResource getTaskResult(ProjectGenerationTask projectGenerationTask) {
        return fileService.getFileResource(projectGenerationTask.getStorageLocation());
    }
}
