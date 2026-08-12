import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

public class server{
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

        server.createContext("/tasks", exchange -> {
            Headers header = exchange.getResponseHeaders();
            header.set("Content-Type", "application/json");

            String body = "[]";
            byte[] responseBytes = body.getBytes();

            exchange.sendResponseHeaders(200, responseBytes.length);

            try(OutputStream stream = exchange.getResponseBody()){
                stream.write(responseBytes);
            }
        });

        server.start();

        System.out.println("Server running on port: 8080");
    }
}