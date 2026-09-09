package com.ShreyasShet.taskserver.repository;

import com.ShreyasShet.taskserver.model.Task;

import org.springframework.stereotype.Component;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TaskRepository {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File dataFile = new File("data/tasks.json");
    private final List<Task> tasks;
    private int nextID;

    public TaskRepository(){
        this.tasks = loadFromDisk();
        this.nextID = computeNextId();
    }

    public List<Task> getAllTasks(){
        return tasks;
    }

    public Task addTask(Task t){
        t.createdAt = Instant.now();
        t.id = nextID++;
        tasks.add(t);
        saveToDisk();
        return t;
    }

    public Optional<Task> getTaskById(int id){
        for(Task t : tasks){
            if (t.getId() == id)
                return Optional.of(t);
        }
        return Optional.empty();
    }

    private List<Task> loadFromDisk(){
        if(!dataFile.exists()){
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(dataFile ,  new TypeReference<List<Task>> () {});
        } catch (Exception e) {
            throw new IllegalStateException("File cannot be read from: " + dataFile.getAbsolutePath(), e);
        }
    }

    private void saveToDisk() {
        try{
            File parentDir = dataFile.getParentFile();
            if (parentDir != null && !parentDir.exists()){
                parentDir.mkdirs();
            }
            mapper.writeValue(dataFile, tasks);
        } catch (Exception e) {
            throw new IllegalStateException("File Cannot be written to: " + dataFile.getAbsolutePath(), e);
        }
    }

    public void deleteTask(int id){
        tasks.removeIf(t -> t.getId() == id);
        saveToDisk();
    }

    private int computeNextId(){
        return tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }
}