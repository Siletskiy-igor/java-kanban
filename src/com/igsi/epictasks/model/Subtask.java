package com.igsi.epictasks.model;

import com.igsi.epictasks.service.TaskStatus;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String title, String description, TaskStatus taskStatus, Integer epicId) {
        super(title, description, taskStatus);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public Subtask copy() {
        Subtask copy = new Subtask(this.getTitle(), this.getDescription(), this.getTaskStatus(), this.getEpicId());
        copy.setId(this.getId());
        return copy;
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
