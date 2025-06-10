package com.igsi.epictasks.service;

import com.igsi.epictasks.model.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();
    void add(Task task);
}
