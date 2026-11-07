package com.ShreyasShet.taskserver.repository;

import com.ShreyasShet.taskserver.model.History;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer>{}