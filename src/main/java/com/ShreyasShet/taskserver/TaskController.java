package com.ShreyasShet.taskserver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    
    public TaskController(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> listAllTasks(){
        return taskRepository.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> listTaskById(@PathVariable int id){
        Optional<Task> maybeTask = taskRepository.getTaskById(id);
        if(maybeTask.isPresent())
            return ResponseEntity.ok(maybeTask.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Task> addNewTask(@RequestBody Task task){
        Task created = taskRepository.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
