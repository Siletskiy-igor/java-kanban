package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks;

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
        this.subtasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
