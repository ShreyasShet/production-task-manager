package com.ShreyasShet.taskserver.model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(
    name = "task_status_history",
    check = @CheckConstraint (
        constraint = "from_status is NULL or from_status <> to_status"
    ),
    indexes = @Index(name = "task_history_changed", columnList = "task_id, changed_at")
)
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(
        name = "task_id", 
        columnDefinition = "integer references task(id) on delete cascade",
        nullable = false, 
        updatable = false
    )
    public Integer taskId;
    @Column(
        name = "from_status", 
        columnDefinition = "varchar(20) check (from_status in ('planned','in-progress','done'))", 
        updatable = false
    )
    public String fromStatus;
    @Column(
        name = "to_status", 
        nullable = false, 
        columnDefinition = "varchar(20) check (to_status in ('planned','in-progress','done'))", 
        updatable = false
    )
    public String toStatus;
    @Column(name = "changed_at", nullable = false, updatable = false)
    public Instant changedAt;

    @PrePersist
    protected void onCreate() { changedAt = Instant.now(); }

    //standard getters
    public Integer getId() { return id; }
    public Integer getTaskId() { return taskId; }
    public String getFromStatus() { return fromStatus; }
    public String getToStatus() { return toStatus; }
    public Instant getChangedAt() { return changedAt; }

    //standard setters
    public void setId(Integer id) { this.id = id; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
}