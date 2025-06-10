package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Epic;
import com.igsi.epictasks.model.Subtask;
import com.igsi.epictasks.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subtaskIdList = epic.getSubtasks();
            for (Integer subtaskId : subtaskIdList) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            Integer epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            ArrayList<Integer> subtasksIdList = epic.getSubtasks();
            Integer subtaskId = subtask.getId();
            subtasksIdList.remove(subtaskId);
            updateStatus(epic);
        }
        subtasks.clear();

    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (subtask.getId() != null && subtask.getId().equals(subtask.getEpicId())) {
            throw new IllegalArgumentException("Subtask cannot have itself as epic");
        }
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic == null) {
            throw new IllegalArgumentException("Epic not found for Subtask");
        }
        int newId = getNextId();
        if (newId == subtask.getEpicId()) {
            throw new IllegalArgumentException("Subtask cannot have itself as epic");
        }
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask.getId());
        updateStatus(epic);
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        Epic oldEpic = getEpicById(newEpic.getId());
        ArrayList<Integer> oldSubtaskIds = oldEpic.getSubtasks();
        ArrayList<Integer> newSubtaskIds = newEpic.getSubtasks();

        for (Integer oldSubtaskId : oldSubtaskIds) {
            if (!newSubtaskIds.contains(oldSubtaskId)) {
                subtasks.remove(oldSubtaskId);
            }
        }
        for (Integer newId : newSubtaskIds) {
            if (!oldSubtaskIds.contains(newId)) {
                Subtask subtask = subtasks.get(newId);
                if (subtask != null) {
                    subtask.setEpicId(newEpic.getId());
                } else {
                    System.out.println("нет такой подзадачи");
                }
            }
        }
        updateStatus(newEpic);
        return newEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        Integer oldEpicId = subtasks.get(newSubtask.getId()).getEpicId();
        Integer newEpicId = newSubtask.getEpicId();
        Epic oldEpic = epics.get(oldEpicId);
        Epic newEpic = epics.get(newEpicId);
        if (!Objects.equals(oldEpicId, newEpicId)) {
            oldEpic.getSubtasks().remove(newSubtask.getId());
            newEpic.getSubtasks().add(newSubtask.getId());
        }

        subtasks.put(newSubtask.getId(), newSubtask);
        updateStatus(newEpic);

        return newSubtask;
    }

    @Override
    public Task removeTaskById(Integer id) {
        return tasks.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        Epic epic = getEpicById(id);
        ArrayList<Integer> subtaskIdList = epic.getSubtasks();
        for (Integer subtaskId : subtaskIdList) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Integer epicId = getSubtaskById(id).getEpicId();
        Epic currentEpic = getEpicById(epicId);
        currentEpic.getSubtasks().remove(id);
        subtasks.remove(id);
        updateStatus(currentEpic);
    }

    @Override
    public ArrayList<Subtask> getSubtaskByEpic(Epic epic) {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Integer element : epic.getSubtasks()) {
            result.add(subtasks.get(element));
        }
        return result;
    }

    private Integer getNextId() {
        return generatorId++;
    }

    private void updateStatus(Epic epic) {
        ArrayList<Integer> subtaskIds = epic.getSubtasks();
        if (subtaskIds.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer id : subtaskIds) {
            TaskStatus status = subtasks.get(id).getTaskStatus();
            if (status != TaskStatus.NEW) {
                allNew = false;
            }

            if (status != TaskStatus.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
