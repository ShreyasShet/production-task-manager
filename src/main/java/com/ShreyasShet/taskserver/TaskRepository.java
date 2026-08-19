package com.ShreyasShet.taskserver;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskRepository {
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;
    public Task addTask(Task task){
        task.id = nextId++;
        task.createdAt = Instant.now();
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks(){
        return tasks;
    }
}
