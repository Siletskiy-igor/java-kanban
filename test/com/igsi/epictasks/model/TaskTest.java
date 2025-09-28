package com.igsi.epictasks.model;

import com.igsi.epictasks.service.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task t1 = new Task("firstTask", "firstDesc", TaskStatus.NEW);
        t1.setId(1);
        Task t2 = new Task("secondTask", "secondDesc", TaskStatus.DONE);
        t2.setId(1);

        assertEquals(t1, t2);
    }

}
