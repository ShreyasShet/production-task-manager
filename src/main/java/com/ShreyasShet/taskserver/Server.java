package com.ShreyasShet.taskserver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.util.List;
import java.util.ArrayList;
import java.time.Instant;

class Task{
    int id;
    String title;
    String status;
    String description;
    Instant createdAt;

    Task(int id, String title, String status, String description){
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        this.createdAt = Instant.now();
    }
    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}

public class Server{
    static List<Task> taskRepository = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        taskRepository.add(new Task(1, "Learn docker", "TODO", "install docker desktop"));
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            String response = "Hello from Task Manager";

            exchange.sendResponseHeaders(200, response.length());

            try (OutputStream stream = exchange.getResponseBody()){
                stream.write(response.getBytes());
            }
        });

        server.createContext("/health", exchange -> {
            Headers header = exchange.getResponseHeaders();
            header.set("Content-Type", "application/json");

            String body = "{\"status\":\"up\"}";
            byte[] responseBytes = body.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream stream = exchange.getResponseBody()){
                stream.write(responseBytes);
            }
        });

        server.createContext("/tasks", exchange -> {
            Headers header = exchange.getResponseHeaders();
            header.set("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            byte[] responseBytes = mapper.writeValueAsBytes(taskRepository);

            exchange.sendResponseHeaders(200, responseBytes.length);

            try(OutputStream stream = exchange.getResponseBody()){
                stream.write(responseBytes);
            }
        });

        server.start();

        System.out.println("Server running on port: 8080");
    }
}