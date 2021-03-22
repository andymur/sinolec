package com.sinolec.challenge.services.task;

import com.sinolec.challenge.exceptions.NotFoundException;
import com.sinolec.challenge.model.GeneralTask;
import com.sinolec.challenge.model.GeneralTaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskRetrieverService {

    private final GeneralTaskRepository generalTaskRepository;

    public TaskRetrieverService(GeneralTaskRepository generalTaskRepository) {
        this.generalTaskRepository = generalTaskRepository;
    }

    public GeneralTask getTask(String taskId) {
        return get(taskId);
    }

    public List<GeneralTask> listTasks() {
        return generalTaskRepository.findAll();
    }

    private GeneralTask get(String taskId) {
        final Optional<GeneralTask> projectGenerationTask = generalTaskRepository.findById(taskId);
        return projectGenerationTask.orElseThrow(NotFoundException::new);
    }
}
