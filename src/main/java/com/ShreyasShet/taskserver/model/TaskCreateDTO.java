package com.ShreyasShet.taskserver.model;

import jakarta.validation.constraints.NotBlank;

public class TaskCreateDTO {
    @NotBlank(message = "title is required")
    private String title;
    private String status;
    private String description;

    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
}