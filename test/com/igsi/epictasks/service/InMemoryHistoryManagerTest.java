package com.igsi.epictasks.service;
import com.igsi.epictasks.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class InMemoryHistoryManagerTest {


    @Test
    public void testCreateTask() {
        TaskManager taskManager = Managers.getDefault();
        taskManager.createTask(new Task("name", "desc", TaskStatus.NEW));
        List<Task> tasks = taskManager.getTasks();
        Assertions.assertEquals(1, tasks.size());
    }
}
