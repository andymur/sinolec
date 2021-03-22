package com.sinolec.challenge.controllers;

import com.sinolec.challenge.services.task.TaskRetrieverService;
import com.sinolec.challenge.services.task.executors.TaskExecutorService;
import com.sinolec.challenge.services.task.updaters.TaskUpdaterService;
import com.sinolec.challenge.test.utils.TaskCreator;
import com.sinolec.challenge.test.utils.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskExecutorService taskExecutorService;

    @MockBean
    private TaskRetrieverService taskRetrieverService;

    @MockBean
    private TaskUpdaterService taskUpdaterService;

    private TaskCreator taskCreator = new TaskCreator();

    @Test
    public void requestWithoutSecurityHeaderShouldReturn401() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_WRONG_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void requestForTaskListWhenEmpty() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();
    }

    @Test
    public void requestForTaskListWhenNotEmpty() throws Exception {
        Mockito.when(taskRetrieverService.listTasks()).thenReturn(
                Collections.singletonList(taskCreator.createProjectGenerationTask("magicTask"))
        );

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..*.name").value("magicTask"))
                .andReturn();

        Mockito.verify(taskRetrieverService, Mockito.times(1)).listTasks();
    }

    @Test
    public void requestForNonExistedParticularTask() {
        //TBD
    }

    @Test
    public void requestForExistedParticularTask() {
        //TBD
    }

    @Test
    public void updateNonExistedTask() {
        //TBD
    }

    @Test
    public void updateExistedTask() {
        //TBD
    }

    @Test
    public void deleteNonExistedTask() {

    }

    @Test
    public void deleteExistedTask() {

    }

    @Test
    public void executeNonExistedTask() {
        //TBD
    }

    @Test
    public void executedTask() {
        //TBD
    }
}
