package com.ShreyasShet.taskserver.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ShreyasShet.taskserver.model.History;
import com.ShreyasShet.taskserver.model.Task;
import com.ShreyasShet.taskserver.repository.HistoryRepository;
import com.ShreyasShet.taskserver.repository.TaskRepository;
import com.ShreyasShet.taskserver.exception.InvalidStatusTransitionException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    private static final Map<String, Set<String>> ALLOWED_TRANSITIONS = Map.of(
        "planned",     Set.of("in-progress"),
        "in-progress", Set.of("planned", "done"),
        "done",        Set.of("in-progress")
    );

    private final TaskRepository taskRepository;
    private final HistoryRepository historyRepository;

    TaskService(TaskRepository taskRepository, HistoryRepository historyRepository) {
        this.taskRepository = taskRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Task getTask(Integer id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found"));
    }

    @Transactional
    public Task createTask(Task task) {
        if (task.getStatus() == null) {
            task.setStatus("planned");
        } else if (!task.getStatus().equals("planned")) {
            throw new IllegalArgumentException("A new task must start with planned status");
        }

        Task saved = taskRepository.save(task);
        recordHistory(saved.getId(), null, saved.getStatus());
        return saved;
    }

    @Transactional
    public Task updateTask(Integer id, Task incoming) {
        Task existing = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found"));

        String oldStatus = existing.getStatus();
        String newStatus = incoming.getStatus();

        if (newStatus == null) {
            throw new IllegalArgumentException("Status is required");
        }

        boolean statusChanged = !oldStatus.equals(newStatus);
        if (statusChanged) {
            validateTransition(oldStatus, newStatus);   // before changing anything
        }

        existing.setTitle(incoming.getTitle());
        existing.setDescription(incoming.getDescription());
        existing.setStatus(newStatus);

        Task saved = taskRepository.save(existing);

        if (statusChanged) {
            recordHistory(id, oldStatus, newStatus);
        }
        return saved;
    }

    @Transactional
    public Task deleteTask(Integer id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found"));

        taskRepository.delete(task);

        return task;
    }

    private void validateTransition(String from, String to) {
        Set<String> allowed = ALLOWED_TRANSITIONS.getOrDefault(from, Set.of());
        if (!allowed.contains(to)) {
            throw new InvalidStatusTransitionException(from, to);
        }
    }

    private void recordHistory(Integer taskId, String from, String to) {
        History h = new History();
        h.setTaskId(taskId);
        h.setFromStatus(from);
        h.setToStatus(to);
        historyRepository.save(h);
    }
}