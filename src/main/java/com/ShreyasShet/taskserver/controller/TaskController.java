package com.ShreyasShet.taskserver.controller;

import com.ShreyasShet.taskserver.model.Task;
import com.ShreyasShet.taskserver.model.TaskCreateDTO;
import com.ShreyasShet.taskserver.model.TaskUpdateDTO;
import com.ShreyasShet.taskserver.service.TaskService;

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

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;
    
    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public List<Task> listAllTasks(){
        return service.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> listTaskById(@PathVariable Integer id){
        Task t = service.getTask(id);
        return ResponseEntity.ok(t); //200 + body
    }

    @PostMapping
    public ResponseEntity<Task> addNewTask(@Valid @RequestBody TaskCreateDTO task){
        Task created = service.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); //201 + body
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTaskById(@PathVariable  Integer id){
        Task deletedTask = service.deleteTask(id);
        return ResponseEntity.ok(deletedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer id, @Valid @RequestBody TaskUpdateDTO updatedTask) {
        Task savedTask = service.updateTask(id, updatedTask);
        return ResponseEntity.ok(savedTask);
    }
}
