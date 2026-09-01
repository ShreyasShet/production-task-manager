package com.ShreyasShet.taskserver;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<Task> getTaskById(int id){
        for(Task t : tasks){
            if (t.getId() == id) 
                return Optional.of(t);   
        }
        return Optional.empty();
    }

    public List<Task> getAllTasks(){
        return tasks;
    }
}
