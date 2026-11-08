package com.ShreyasShet.taskserver.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskUpdateDTO {
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "mention task status")
    private String status;
    private String description;
    @NotNull(message = "version is required")
    private Long version;

    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public Long getVersion() { return version; }

    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setVersion(Long version) { this.version = version; }
}