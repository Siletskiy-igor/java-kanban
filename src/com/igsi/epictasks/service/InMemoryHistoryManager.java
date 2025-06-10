package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Epic;
import com.igsi.epictasks.model.Subtask;
import com.igsi.epictasks.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> history = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10;
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        
        Task copy;
        if (task instanceof Subtask) {
            copy = ((Subtask) task).copy();
        } else if (task instanceof Epic) {
            copy = ((Epic) task).copy();
        } else {
            copy = task.copy();
        }
        history.add(copy);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }

}
