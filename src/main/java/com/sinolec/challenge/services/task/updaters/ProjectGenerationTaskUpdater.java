package com.sinolec.challenge.services.task.updaters;

import com.sinolec.challenge.model.ProjectGenerationTask;
import org.springframework.stereotype.Component;

@Component
public class ProjectGenerationTaskUpdater implements GeneralTaskUpdater<ProjectGenerationTask> {

    @Override
    public ProjectGenerationTask update(ProjectGenerationTask existing, ProjectGenerationTask updatedTask) {
        existing.setCreationDate(updatedTask.getCreationDate());
        existing.setName(updatedTask.getName());
        return existing;
    }
}
