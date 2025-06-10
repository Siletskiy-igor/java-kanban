package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask s1 = new Subtask("firstSubtask", "firstDesc", TaskStatus.NEW, 1);
        s1.setId(10);
        Subtask s2 = new Subtask("secondSubtask", "secondDesc", TaskStatus.IN_PROGRESS, 99);
        s2.setId(10);

        assertEquals(s1, s2);
    }
}
