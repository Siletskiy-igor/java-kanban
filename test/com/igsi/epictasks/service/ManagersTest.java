package com.igsi.epictasks.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    TaskManager taskManager;
    HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void managersUtilityClassReturnsInitializedInstances() {
        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }
}
