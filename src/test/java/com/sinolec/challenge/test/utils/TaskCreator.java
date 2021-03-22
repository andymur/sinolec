package com.sinolec.challenge.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinolec.challenge.model.*;

import java.util.Date;
import java.util.UUID;

public class TaskCreator {

    public ProjectGenerationTask createProjectGenerationTask(String taskName) {
        final ProjectGenerationTask task = new ProjectGenerationTask();
        task.setId(UUID.randomUUID().toString());
        task.setName(taskName);
        task.setCreationDate(new Date());
        task.setType(TaskType.PROJECT_GENERATION_TASK);
        return task;
    }

    public TaskProgress createTaskProgressOutOfJsonString(String taskProgressAsJsonString) {
        try {
            return new ObjectMapper().readValue(taskProgressAsJsonString, TaskProgress.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createProjectGenerationTaskAsJsonString(String taskName) {
        return translateTaskAsJsonString(createProjectGenerationTask(taskName));
    }

    public ProjectGenerationTask createProjectGenerationOutOfJsonString(String projectGenerationTaskAsJsonString) {
        try {
            return new ObjectMapper().readValue(projectGenerationTaskAsJsonString, ProjectGenerationTask.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CounterTask createCounterTask(String taskName, int x, int y) {
        final CounterTask task = new CounterTask();
        task.setId(UUID.randomUUID().toString());
        task.setName(taskName);
        task.setCreationDate(new Date());
        task.setX(x);
        task.setY(y);
        task.setType(TaskType.COUNTER_TASK);
        return task;
    }

    public String createCounterTaskAsJsonString(String taskName, int x, int y) {
        return translateTaskAsJsonString(createCounterTask(taskName, x, y));
    }

    public CounterTask createCounterTaskOutOfJsonString(String counterTaskAsJsonString) {
        try {
            return new ObjectMapper().readValue(counterTaskAsJsonString, CounterTask.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static String translateTaskAsJsonString(GeneralTask task) {
        try {
            return new ObjectMapper().writeValueAsString(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
