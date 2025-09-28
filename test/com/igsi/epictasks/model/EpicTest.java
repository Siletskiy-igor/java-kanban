package com.igsi.epictasks.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("Epic One", "First epic");
        epic1.setId(42);

        Epic epic2 = new Epic("Another Epic", "Different description");
        epic2.setId(42);

        assertEquals(epic1, epic2);
    }

}
