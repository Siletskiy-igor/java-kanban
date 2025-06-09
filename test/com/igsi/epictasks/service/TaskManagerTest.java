package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Task;
import com.igsi.epictasks.model.Subtask;
import com.igsi.epictasks.model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task t1 = new Task("firstTask", "firstDesc", TaskStatus.NEW);
        t1.setId(1);
        Task t2 = new Task("secondTask", "secondDesc", TaskStatus.DONE);
        t2.setId(1);

        assertEquals(t1, t2);
    }

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask s1 = new Subtask("firstSubtask", "firstDesc", TaskStatus.NEW, 1);
        s1.setId(10);
        Subtask s2 = new Subtask("secondSubtask", "secondDesc", TaskStatus.IN_PROGRESS, 99);
        s2.setId(10);

        assertEquals(s1, s2);
    }

    @Test
    void epicCannotContainItselfAsSubtask() {
        Epic epic = new Epic("Epic", "desc");
        int epicId = taskManager.createEpic(epic).getId();

        Subtask subtask = new Subtask("Subtask", "desc", TaskStatus.NEW, epicId);
        subtask.setId(epicId);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createSubtask(subtask);
        });
        assertTrue(e.getMessage().contains("Subtask cannot have itself as epic"));
    }

    @Test
    void subtaskCannotReferToItselfAsEpic() {
        Subtask sub = new Subtask("Sub", "desc", TaskStatus.NEW, 1);
        sub.setId(10);
        sub.setEpicId(10);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.createSubtask(sub);
        });
        assertTrue(e.getMessage().contains("Subtask cannot have itself as epic"));
    }

    @Test
    void managersUtilityClassReturnsInitializedInstances() {
        TaskManager tm = Managers.getDefault();
        HistoryManager hm = Managers.getDefaultHistory();

        assertNotNull(tm);
        assertNotNull(hm);
    }

    @Test
    void inMemoryTaskManagerAddsAndRetrievesTasksCorrectly() {
        Task task = new Task("Task", "TaskDesc", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "EpicDesc");
        int epicId = taskManager.createEpic(epic).getId();
        Subtask subtask = new Subtask("Subtask", "SubtaskDesc", TaskStatus.NEW, epicId);

        int taskId = taskManager.createTask(task).getId();
        int subId = taskManager.createSubtask(subtask).getId();

        assertEquals(task, taskManager.getTaskById(taskId));
        assertEquals(epic, taskManager.getEpicById(epicId));
        assertEquals(subtask, taskManager.getSubtaskById(subId));
    }

    @Test
    void idConflictsHandledBetweenManualAndGeneratedIds() {
        Task manual = new Task("Manual", "desc", TaskStatus.NEW);
        manual.setId(100);
        taskManager.createTask(manual);

        Task generated = new Task("Generated", "desc", TaskStatus.NEW);
        int id = taskManager.createTask(generated).getId();

        assertNotEquals(100, id);
    }

    @Test
    void taskImmutabilityAfterAddition() {
        Task original = new Task("Original", "Desc", TaskStatus.NEW);
        int id = taskManager.createTask(original).getId();
        Task retrieved = taskManager.getTaskById(id);

        assertEquals(original.getTitle(), retrieved.getTitle());
        assertEquals(original.getDescription(), retrieved.getDescription());
        assertEquals(original.getTaskStatus(), retrieved.getTaskStatus());
    }

    @Test
    void historyManagerStoresPreviousVersion() {
        Task task = new Task("firstversion", "desc", TaskStatus.NEW);
        int id = taskManager.createTask(task).getId();
        taskManager.getTaskById(id);

        Task modified = new Task("secondversion", "desc2", TaskStatus.DONE);
        modified.setId(id);

        taskManager.updateTask(modified);
        taskManager.getTaskById(id);

        List<Task> history = taskManager.getHistory();
        assertFalse(history.isEmpty());

        Task first = history.getFirst();
        Task second = history.getLast();

        assertEquals(2, history.size());
        assertEquals("firstversion", first.getTitle());
        assertEquals("desc", first.getDescription());
        assertEquals(TaskStatus.NEW, first.getTaskStatus());

        assertEquals("secondversion", second.getTitle());
        assertEquals("desc2", second.getDescription());
        assertEquals(TaskStatus.DONE, second.getTaskStatus());

    }
}

