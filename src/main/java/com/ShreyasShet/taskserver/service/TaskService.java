package com.ShreyasShet.taskserver.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ShreyasShet.taskserver.model.History;
import com.ShreyasShet.taskserver.model.Task;
import com.ShreyasShet.taskserver.model.TaskCreateDTO;
import com.ShreyasShet.taskserver.model.TaskUpdateDTO;
import com.ShreyasShet.taskserver.repository.HistoryRepository;
import com.ShreyasShet.taskserver.repository.TaskRepository;
import com.ShreyasShet.taskserver.exception.InvalidStatusTransitionException;
import com.ShreyasShet.taskserver.exception.ConflictException;

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
    public Task createTask(TaskCreateDTO request) {
        if (request.getStatus() == null) {
            request.setStatus("planned");
        } else if (!request.getStatus().equals("planned")) {
            throw new IllegalArgumentException("A new task must start with planned status");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus("planned");
        Task saved = taskRepository.save(task);
        recordHistory(saved.getId(), null, saved.getStatus());
        return saved;
    }

    @Transactional
    public Task updateTask(Integer id, TaskUpdateDTO request) {
        Task existing = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task " + id + " not found"));

        if (!existing.getVersion().equals(request.getVersion())) {
                throw new ConflictException("Task was modified by someone else");
        }

        String oldStatus = existing.getStatus();
        String newStatus = request.getStatus();

        if (newStatus == null) {
            throw new IllegalArgumentException("Status is required");
        }

        boolean statusChanged = !oldStatus.equals(newStatus);
        if (statusChanged) {
            validateTransition(oldStatus, newStatus);   // before changing anything
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setStatus(newStatus);

        if (statusChanged) {
            recordHistory(id, oldStatus, newStatus);
        }
        return existing;
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