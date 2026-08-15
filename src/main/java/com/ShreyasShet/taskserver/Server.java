package com.ShreyasShet.taskserver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    public Task() {}
    
    //setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    //getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}

public class Server{
    static List<Task> taskRepository = new ArrayList<>();
    private static int nextId = 1;
    public static void main(String[] args) throws IOException {
        
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

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        server.createContext("/tasks", exchange -> {
            try {
                String method = exchange.getRequestMethod();
                if ("POST".equals(method)) {
                    Task task = mapper.readValue(exchange.getRequestBody(), Task.class);

                    task.setId(nextId++);
                    task.setCreatedAt(Instant.now());

                    taskRepository.add(task);

                    byte[] response = mapper.writeValueAsBytes(task);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(201, response.length);
                    try (OutputStream stream = exchange.getResponseBody()) {
                        stream.write(response);
                    }
                } else if ("GET".equals(method)) {
                    Headers header = exchange.getResponseHeaders();
                    header.set("Content-Type", "application/json");
                    byte[] responseBytes = mapper.writeValueAsBytes(taskRepository);
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    try (OutputStream stream = exchange.getResponseBody()) {
                        stream.write(responseBytes);
                    }
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
                exchange.close();
            }
        });

        server.start();

        System.out.println("Server running on port: 8080");
    }
}