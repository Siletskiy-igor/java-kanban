package tasks;

public class Subtask extends Task {
    private final Integer epicId;

    public Subtask(String title, String description, TaskStatus taskStatus, Integer epicId) {
        super(title, description, taskStatus);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
