import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 1;

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            ArrayList<Integer> subtaskIdList = epic.getSubtasks();
            for (Integer subtaskId : subtaskIdList) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

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

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        epic.getSubtasks().add(subtask.getId());
        updateStatus(epic);
        return subtask;
    }

    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic updateEpic(Epic newEpic) {
        Epic oldEpic = getEpicById(newEpic.getId());
        ArrayList<Integer> oldEpicSubtaskIdList = oldEpic.getSubtasks();
        for (Integer oldEpicSubtaskId : oldEpicSubtaskIdList) {
            if (!newEpic.getSubtasks().contains(oldEpicSubtaskId)) {
                subtasks.remove(oldEpicSubtaskId);
            }
        }
        epics.put(newEpic.getId(), newEpic);
        updateStatus(newEpic);
        return newEpic;
    }

    public Subtask updateSubtask(Subtask newSubtask) {
        Integer oldEpicId = subtasks.get(newSubtask.getId()).getEpicId();
        Integer newEpicId = newSubtask.getEpicId();
        if (!Objects.equals(oldEpicId, newEpicId)) {
            epics.get(oldEpicId).getSubtasks().remove(newSubtask.getId());
        }
        createSubtask(newSubtask);
        updateStatus(epics.get(newSubtask.getEpicId()));
        return newSubtask;
    }

    public Task removeTaskById(Integer id) {
        return tasks.remove(id);
    }

    public void removeEpicById(Integer id) {
        Epic epic = getEpicById(id);
        ArrayList<Integer> subtaskIdList = epic.getSubtasks();
        for (Integer subtaskId : subtaskIdList) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void removeSubtaskById(Integer id) {
        Integer epicId = getSubtaskById(id).getEpicId();
        Epic currentEpic = getEpicById(epicId);
        currentEpic.getSubtasks().remove(id);
        subtasks.remove(id);
        updateStatus(currentEpic);
    }

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
        TaskStatus epicStatus = epic.getTaskStatus();

        if (epic.getSubtasks().isEmpty()) {
            epicStatus = TaskStatus.NEW;
        }
        int s = epic.getSubtasks().size();
        for (int i = 0; i < s; i++) {
            TaskStatus initialStatus = subtasks.get(epic.getSubtasks().get(0)).getTaskStatus();
            TaskStatus currentStatus = subtasks.get(epic.getSubtasks().get(i)).getTaskStatus();
            if (currentStatus == initialStatus) {
                epicStatus = currentStatus;
            } else {
                epicStatus = TaskStatus.IN_PROGRESS;
            }
        }
        epic.setTaskStatus(epicStatus);
    }


}
