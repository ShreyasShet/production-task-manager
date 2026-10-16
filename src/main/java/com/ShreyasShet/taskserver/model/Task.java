package com.ShreyasShet.taskserver.model;

import java.time.Instant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    public String title;
    @Column(nullable = false, columnDefinition = "varchar(20) check (status in ('planned','in-progress','done'))")
    public String status;
    public String description;
    @Column(nullable = false, updatable = false)
    public Instant createdAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); }

    //standard getters
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    //standard setters
    public void setId(Integer id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}