package com.ShreyasShet.taskserver.model;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;

public class Task {
    public int id;
    @NotBlank(message = "Title is required")
    public String title;
    public String status;
    public String description;
    public Instant createdAt;

    //standard getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    //standard setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
