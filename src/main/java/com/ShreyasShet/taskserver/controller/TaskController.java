package com.ShreyasShet.taskserver.controller;

import com.ShreyasShet.taskserver.model.Task;
import com.ShreyasShet.taskserver.repository.TaskRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

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
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> listTaskById(@PathVariable Integer id){
        Optional<Task> maybeTask = taskRepository.findById(id);
        if(maybeTask.isPresent())
            return ResponseEntity.ok(maybeTask.get()); //200 + body
        else
            return ResponseEntity.notFound().build(); //404 + body
    }

    @PostMapping
    public ResponseEntity<Task> addNewTask(@Valid @RequestBody Task task){
        Task created = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); //201 + body
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTaskById(@PathVariable  Integer id){
        Optional<Task> maybeTask = taskRepository.findById(id);
        if(maybeTask.isPresent()){
            taskRepository.deleteById(id);
            return ResponseEntity.accepted().body(maybeTask.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @Valid @RequestBody Task updatedTask) {
        return taskRepository.findById(id)
            .map(existingTask -> {
                existingTask.setTitle(updatedTask.getTitle());
                existingTask.setStatus(updatedTask.getStatus());
                existingTask.setDescription(updatedTask.getDescription());
                
                Task savedTask = taskRepository.save(existingTask);

                return ResponseEntity.ok(savedTask);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
