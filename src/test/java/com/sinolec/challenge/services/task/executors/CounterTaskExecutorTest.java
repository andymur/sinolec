package com.sinolec.challenge.services.task.executors;

import com.sinolec.challenge.model.CounterTask;
import com.sinolec.challenge.model.TaskProgress;
import com.sinolec.challenge.services.task.updaters.TaskUpdaterService;
import com.sinolec.challenge.test.utils.TaskCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CounterTaskExecutorTest {

    @Mock
    TaskUpdaterService taskUpdaterService;

    private TaskCreator taskCreator = new TaskCreator();

    @Test
    public void progressOfExecutedTaskMustBeBetweenXAndY() {
        // we just use fake time as 5 seconds after unix epoch started, it is current time for the test
        final CounterTaskExecutor counterTaskExecutor = new CounterTaskExecutor(taskUpdaterService);
        counterTaskExecutor.setTimeSourceValue(5L);
        final CounterTask simpleCounterTask = taskCreator.createCounterTask("simpleCounter", 0, 100);
        //let's pretend that task was executed at 2 seconds after unix epoch begin
        counterTaskExecutor.executeTask(simpleCounterTask, 2L);
        final TaskProgress taskProgress = counterTaskExecutor.getTaskProgress(simpleCounterTask);
        // task got some progress (counter equals to 3) since it has been started at 2 seconds unix epoch and has been measured at 5 seconds
        Assertions.assertEquals(3L, taskProgress.getProgress());
    }

    @Test
    public void progressOfExecutedTaskMustYMoreThanYSecondsFromBeginning() {
        // we just use fake time as 8 seconds after unix epoch started, it is current time for the test
        final CounterTaskExecutor counterTaskExecutor = new CounterTaskExecutor(taskUpdaterService);
        counterTaskExecutor.setTimeSourceValue(8L);
        final CounterTask simpleCounterTask = taskCreator.createCounterTask("simpleCounter", 5, 10);
        //let's pretend that task was executed at 2 seconds after unix epoch begin
        counterTaskExecutor.executeTask(simpleCounterTask, 2L);
        final TaskProgress taskProgress = counterTaskExecutor.getTaskProgress(simpleCounterTask);
        // task got some progress (counter equals to 10) since it has been started at 2 seconds unix epoch and has been measured at 8 seconds
        // which means that after 6 seconds the counter should be x + 6 = 5 + 6 = 11 seconds but it cannot be more than y (10)
        Assertions.assertEquals(10L, taskProgress.getProgress());
    }

    @Test
    public void progressOfCancelledTaskMustStayAtTheTimeOfCancellation() {
        // we just use fake time as 8 seconds after unix epoch started, it is current time for the test
        final CounterTaskExecutor counterTaskExecutor = new CounterTaskExecutor(taskUpdaterService);
        counterTaskExecutor.setTimeSourceValue(10L);
        final CounterTask simpleCounterTask = taskCreator.createCounterTask("simpleCounter", 5, 10);
        //let's pretend that task was executed at 2 seconds after unix epoch begin
        counterTaskExecutor.executeTask(simpleCounterTask, 2L);
        //let's pretend that task was cancelled at 6 seconds after unix epoch begin
        counterTaskExecutor.cancelTask(simpleCounterTask, 6L);
        final TaskProgress taskProgress = counterTaskExecutor.getTaskProgress(simpleCounterTask);

        // so here we measured task's progress at 10 seconds, it is started at 2 seconds and cancelled at 6 seconds.
        // which means that after 4 seconds of execution progress stopped
        // it could be more progress cause x + 4 = 9 lesser than y (10) but since task was cancelled it will be 9 for ever.
        Assertions.assertEquals(9L, taskProgress.getProgress());
        Assertions.assertTrue(simpleCounterTask.isExecuted());
        Assertions.assertTrue(simpleCounterTask.isCancelled());
    }

    @Test
    public void progressOfNonExecutedTaskMustBeEqualsToX() {
        // we just use fake time as 8 seconds after unix epoch started, it is current time for the test
        final CounterTaskExecutor counterTaskExecutor = new CounterTaskExecutor(taskUpdaterService);
        counterTaskExecutor.setTimeSourceValue(8L);
        final CounterTask simpleCounterTask = taskCreator.createCounterTask("simpleCounter", 5, 10);

        final TaskProgress taskProgress = counterTaskExecutor.getTaskProgress(simpleCounterTask);
        Assertions.assertEquals(5L, taskProgress.getProgress());
    }

    @Test
    public void cannotExecuteCancelledTask() {
        final CounterTaskExecutor counterTaskExecutor = new CounterTaskExecutor(taskUpdaterService);
        final CounterTask simpleCounterTask = taskCreator.createCounterTask("simpleCounter", 5, 10);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            counterTaskExecutor.cancelTask(simpleCounterTask);
            counterTaskExecutor.executeTask(simpleCounterTask);
        });
    }

    @Test
    public void cannotCancelOneTaskTwice() {
        final CounterTaskExecutor counterTaskExecutor = new CounterTaskExecutor(taskUpdaterService);
        final CounterTask simpleCounterTask = taskCreator.createCounterTask("simpleCounter", 5, 10);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            counterTaskExecutor.executeTask(simpleCounterTask);
            counterTaskExecutor.executeTask(simpleCounterTask);
        });
    }
}