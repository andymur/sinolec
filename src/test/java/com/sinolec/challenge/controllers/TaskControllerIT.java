package com.sinolec.challenge.controllers;

import com.sinolec.challenge.model.CounterTask;
import com.sinolec.challenge.model.GeneralTask;
import com.sinolec.challenge.model.ProjectGenerationTask;
import com.sinolec.challenge.model.TaskProgress;
import com.sinolec.challenge.test.utils.TaskCreator;
import com.sinolec.challenge.test.utils.TestConstants;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {

    private static final String BASE_URL = "/api/tasks/";
    private static final String BASE_URL_WITH_TASK = BASE_URL.concat("{taskId}");

    private static final String EXECUTE_TASK_URL = BASE_URL_WITH_TASK.concat("/execute");
    private static final String GETTING_PROGRESS_TASK_URL = BASE_URL_WITH_TASK.concat("/progress");
    private static final String CANCEL_TASK_URL = BASE_URL_WITH_TASK.concat("/cancel");

    private static final String PROJECT_GENERATION_TASK_NAME = "superProject";
    private static final String PROJECT_GENERATION_TASK_NEW_NAME = "superDuperProject";

    private static final String COUNTER_TASK_NAME = "magicCounter";
    private static final String COUNTER_TASK_NEW_NAME = "voodooCounter";
    private static final int COUNTER_TASK_START = 0;
    private static final int COUNTER_TASK_FINISH = 10;

    private TaskCreator taskCreator = new TaskCreator();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testValidation() throws Exception {
        RequestBuilder addingInvalidCounterTaskRequest = createAddingCounterTaskRequest(COUNTER_TASK_NAME,
                COUNTER_TASK_FINISH, COUNTER_TASK_START);

        // try to add invalid counter task
        final MvcResult counterTaskCreationResult = mockMvc.perform(addingInvalidCounterTaskRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testCrudOperations() throws Exception {

        RequestBuilder gettingTaskListRequest = MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        RequestBuilder addingCounterTaskRequest = createAddingCounterTaskRequest(COUNTER_TASK_NAME,
                COUNTER_TASK_START, COUNTER_TASK_FINISH);

        RequestBuilder addingProjectGenerationTaskRequest = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE)
                .content(taskCreator.createProjectGenerationTaskAsJsonString(PROJECT_GENERATION_TASK_NAME));

        // check that we have no tasks
        mockMvc.perform(gettingTaskListRequest)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();

        // add counter task
        final MvcResult counterTaskCreationResult = mockMvc.perform(addingCounterTaskRequest)
                .andExpect(status().isOk())
                .andReturn();

        final CounterTask counterTask = taskCreator.createCounterTaskOutOfJsonString(
                counterTaskCreationResult.getResponse().getContentAsString());

        // add project generation task
        final MvcResult projectGenerationTaskResult = mockMvc.perform(addingProjectGenerationTaskRequest)
                .andExpect(status().isOk())
                .andReturn();

        final ProjectGenerationTask projectGenerationTask = taskCreator.createProjectGenerationOutOfJsonString(
                projectGenerationTaskResult.getResponse().getContentAsString());

        // request existed counter task

        RequestBuilder gettingCounterTaskByIdRequest = createGettingTaskByIdRequest(counterTask.getId());

        mockMvc.perform(gettingCounterTaskByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(COUNTER_TASK_NAME))
                .andExpect(jsonPath("$.x").value(COUNTER_TASK_START))
                .andExpect(jsonPath("$.y").value(COUNTER_TASK_FINISH))
                .andReturn();

        // request existed project generation task

        RequestBuilder gettingProjectGenerationTaskByIdRequest = createGettingTaskByIdRequest(projectGenerationTask.getId());

        mockMvc.perform(gettingProjectGenerationTaskByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PROJECT_GENERATION_TASK_NAME))
                .andReturn();

        // update counter task
        final CounterTask updatedCounterTask = counterTask;
        updatedCounterTask.setName(COUNTER_TASK_NEW_NAME);
        updatedCounterTask.setX(COUNTER_TASK_START + 1);
        updatedCounterTask.setY(COUNTER_TASK_FINISH + 1);
        RequestBuilder updatingCounterTaskByIdRequest = createUpdatingTaskByIdRequest(counterTask.getId(),
                updatedCounterTask);

        mockMvc.perform(updatingCounterTaskByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(COUNTER_TASK_NEW_NAME))
                .andExpect(jsonPath("$.x").value(COUNTER_TASK_START + 1))
                .andExpect(jsonPath("$.y").value(COUNTER_TASK_FINISH + 1))
                .andReturn();

        // update project generation task
        final ProjectGenerationTask updatedProjectGenerationTask = projectGenerationTask;
        updatedProjectGenerationTask.setName(PROJECT_GENERATION_TASK_NEW_NAME);

        RequestBuilder updatingProjectGenerationTaskByIdRequest = createUpdatingTaskByIdRequest(projectGenerationTask.getId(),
                updatedProjectGenerationTask);

        mockMvc.perform(updatingProjectGenerationTaskByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PROJECT_GENERATION_TASK_NEW_NAME))
                .andReturn();

        // retrieve counter task again

        mockMvc.perform(gettingCounterTaskByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(COUNTER_TASK_NEW_NAME))
                .andExpect(jsonPath("$.x").value(COUNTER_TASK_START + 1))
                .andExpect(jsonPath("$.y").value(COUNTER_TASK_FINISH + 1))
                .andReturn();

        // retrieve project generation task again

        mockMvc.perform(gettingProjectGenerationTaskByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(PROJECT_GENERATION_TASK_NEW_NAME))
                .andReturn();

        // delete counter task

        mockMvc.perform(createDeletingTaskByIdRequest(counterTask.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        // check that we have just one task in the list

        mockMvc.perform(gettingTaskListRequest)
                .andExpect(status().isOk())
                // should be more elegant way :-)
                .andExpect(content().string(CoreMatchers.not(CoreMatchers.containsString(COUNTER_TASK_NEW_NAME))))
                .andExpect(content().string(CoreMatchers.containsString(PROJECT_GENERATION_TASK_NEW_NAME)))
                .andReturn();

        // request deleted counter task

        mockMvc.perform(gettingCounterTaskByIdRequest)
                .andExpect(status().isNotFound())
                .andReturn();

        // delete project generation task

        mockMvc.perform(createDeletingTaskByIdRequest(projectGenerationTask.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        // check that we have empty list as in the beginning of the test
        mockMvc.perform(gettingTaskListRequest)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();
    }

    @Test
    public void testCounterTaskExecutionFlow() throws Exception {
        RequestBuilder addingCounterTaskRequest = createAddingCounterTaskRequest(COUNTER_TASK_NAME, 100, 500);
        // create counter task
        final MvcResult counterTaskCreationResult = mockMvc.perform(addingCounterTaskRequest)
                .andExpect(status().isOk())
                .andReturn();

        final CounterTask counterTask = taskCreator.createCounterTaskOutOfJsonString(
                counterTaskCreationResult.getResponse().getContentAsString());
        // execute it
        final MockHttpServletRequestBuilder executeRequest = MockMvcRequestBuilders.post(EXECUTE_TASK_URL, counterTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        mockMvc.perform(executeRequest)
                .andExpect(status().isNoContent())
                .andReturn();

        // get some progress
        final MockHttpServletRequestBuilder progressRequest = MockMvcRequestBuilders.get(GETTING_PROGRESS_TASK_URL, counterTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        mockMvc.perform(progressRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progress").value(100))
                .andReturn();

        // common, it's integration test :-)
        Thread.sleep(5000L);

        // get progress again
        mockMvc.perform(progressRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progress").value(Matchers.greaterThan(104)))
                .andReturn();

        // cancel it
        final MockHttpServletRequestBuilder cancelRequest = MockMvcRequestBuilders.post(CANCEL_TASK_URL, counterTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        mockMvc.perform(cancelRequest)
                .andExpect(status().isNoContent())
                .andReturn();

        // get a bit more of progress and see it stopped increasing
        MockMvcRequestBuilders.get(GETTING_PROGRESS_TASK_URL, counterTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);

        final MvcResult progressRequestResult = mockMvc.perform(progressRequest)
                .andExpect(status().isOk())
                .andReturn();

        final TaskProgress progressResult = taskCreator.createTaskProgressOutOfJsonString(progressRequestResult.getResponse().getContentAsString());

        // waiting a bit to make sure progress has been stopped
        Thread.sleep(2000L);

        mockMvc.perform(progressRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.progress").value(progressResult.getProgress()))
                .andReturn();

        // delete counter task
        mockMvc.perform(createDeletingTaskByIdRequest(counterTask.getId()))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private MockHttpServletRequestBuilder createAddingCounterTaskRequest(String taskName,
                                                                         int taskStartValue,
                                                                         int taskFinishValue) {
        return MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE)
                .content(taskCreator.createCounterTaskAsJsonString(taskName, taskStartValue , taskFinishValue));
    }

    private MockHttpServletRequestBuilder createDeletingTaskByIdRequest(String taskId) {
        return MockMvcRequestBuilders.delete(BASE_URL_WITH_TASK, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);
    }

    private MockHttpServletRequestBuilder createUpdatingTaskByIdRequest(String taskId,
                                                                        GeneralTask task) {
        return MockMvcRequestBuilders.put(BASE_URL_WITH_TASK, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE)
                .content(TaskCreator.translateTaskAsJsonString(task));
    }

    private MockHttpServletRequestBuilder createGettingTaskByIdRequest(String taskId) {
        return MockMvcRequestBuilders.get(BASE_URL_WITH_TASK, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(TestConstants.SECURITY_HEADER, TestConstants.SECURITY_HEADER_VALUE);
    }
}
