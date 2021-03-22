package com.sinolec.challenge.services.task.updaters;

import com.sinolec.challenge.model.GeneralTask;
import com.sinolec.challenge.model.GeneralTaskRepository;
import com.sinolec.challenge.model.TaskType;
import com.sinolec.challenge.services.task.TaskRetrieverService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskUpdaterService {

    private final GeneralTaskRepository generalTaskRepository;
    private final TaskRetrieverService taskRetrieverService;

    private final CounterTaskUpdater counterTaskUpdater;
    private final ProjectGenerationTaskUpdater projectGenerationTaskUpdater;

    public TaskUpdaterService(GeneralTaskRepository generalTaskRepository,
                              TaskRetrieverService taskRetrieverService,
                              ProjectGenerationTaskUpdater projectGenerationTaskUpdater,
                              CounterTaskUpdater counterTaskUpdater) {
        this.generalTaskRepository = generalTaskRepository;
        this.taskRetrieverService = taskRetrieverService;
        this.projectGenerationTaskUpdater = projectGenerationTaskUpdater;
        this.counterTaskUpdater = counterTaskUpdater;
    }

    public void delete(String taskId) {
        generalTaskRepository.deleteById(taskId);
    }

    // validate that x < y! for counter & required) & taskType is required for all
    public GeneralTask createTask(GeneralTask task) {
        task.setId(null);
        task.setCreationDate(new Date());
        return generalTaskRepository.save(task);
    }

    public GeneralTask update(String taskId, GeneralTask task) {
        GeneralTask existing = taskRetrieverService.getTask(taskId);
        return generalTaskRepository.save(chooseUpdater(existing.getType()).update(existing, task));
    }

    @SuppressWarnings("unchecked")
    private <T extends GeneralTask> GeneralTaskUpdater<T> chooseUpdater(TaskType taskType) {
        switch (taskType) {
            case PROJECT_GENERATION_TASK:
                return (GeneralTaskUpdater<T>) projectGenerationTaskUpdater;
            case COUNTER_TASK:
                return (GeneralTaskUpdater<T>) counterTaskUpdater;
        }
        throw new IllegalArgumentException("Unknown task with a type of: " + taskType);
    }
}
