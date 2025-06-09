package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Task;

import java.util.List;

public interface HistoryManager {
    public List<Task> getHistory();
    public void add(Task task);
}
