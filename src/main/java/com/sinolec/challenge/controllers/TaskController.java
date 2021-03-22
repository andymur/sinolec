package com.sinolec.challenge.controllers;

import com.sinolec.challenge.exceptions.InvalidTaskException;
import com.sinolec.challenge.model.GeneralTask;
import com.sinolec.challenge.model.TaskProgress;
import com.sinolec.challenge.services.task.TaskRetrieverService;
import com.sinolec.challenge.services.task.executors.TaskExecutorService;
import com.sinolec.challenge.services.task.updaters.TaskUpdaterService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskExecutorService taskExecutorService;
    private final TaskRetrieverService taskRetrieverService;
    private final TaskUpdaterService taskUpdaterService;

    public TaskController(TaskExecutorService taskExecutorService,
                          TaskRetrieverService taskRetrieverService,
                          TaskUpdaterService taskUpdaterService) {
        this.taskExecutorService = taskExecutorService;
        this.taskRetrieverService = taskRetrieverService;
        this.taskUpdaterService = taskUpdaterService;
    }

    @GetMapping("/")
    public List<GeneralTask> listTasks() {
        return taskRetrieverService.listTasks();
    }

    @PostMapping("/")
    public GeneralTask createTask(@RequestBody @Valid GeneralTask task, Errors errors) {
        task.validate().ifPresent(errors::reject);
        if (errors.hasErrors()) {
            throw new InvalidTaskException();
        }
        return taskUpdaterService.createTask(task);
    }

    @GetMapping("/{taskId}")
    public GeneralTask getTask(@PathVariable String taskId) {
        return taskRetrieverService.getTask(taskId);
    }

    @PutMapping("/{taskId}")
    public GeneralTask updateTask(@PathVariable String taskId,
                                  @RequestBody @Valid GeneralTask task,
                                  Errors errors) {
        task.validate().ifPresent(errors::reject);
        if (errors.hasErrors()) {
            throw new InvalidTaskException();
        }
        return taskUpdaterService.update(taskId, task);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String taskId) {
        taskUpdaterService.delete(taskId);
    }

    @PostMapping("/{taskId}/execute")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void executeTask(@PathVariable String taskId) {
        taskExecutorService.executeTask(taskId);
    }

    @PostMapping("/{taskId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTask(@PathVariable String taskId) {
        taskExecutorService.cancelTask(taskId);
    }

    @GetMapping("/{taskId}/progress")
    public TaskProgress getProgress(@PathVariable String taskId) {
        return taskExecutorService.getTaskProgress(taskId);
    }

    @GetMapping("/{taskId}/result")
    public ResponseEntity<FileSystemResource> getResult(@PathVariable String taskId) {
        final FileSystemResource fileResult = taskExecutorService.getTaskResult(taskId);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentDispositionFormData("attachment", "challenge.zip");

        return new ResponseEntity<>(fileResult, respHeaders, HttpStatus.OK);
    }

}
