package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void addOneTask() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    void addSomeTasks() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task1, task2, task3), history);
    }

    @Test
    void addDuplicateTask() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task2, task1), history);
    }

    @Test
    void removeTaskInTheMiddle() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task1, task3), history);
    }

    @Test
    void removeTaskFromBeginningAndEnd() {
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);
        historyManager.remove(3);

        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task2), history);
    }

    @Test
    void getEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void addNullTask() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
