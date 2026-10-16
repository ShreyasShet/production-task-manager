package com.ShreyasShet.taskserver.repository;

import com.ShreyasShet.taskserver.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer>{}