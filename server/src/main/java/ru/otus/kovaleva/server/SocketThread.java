package ru.otus.kovaleva.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class SocketThread extends Thread {
    private Socket clientSocket;
    private Map<String, MyWebApplication> router;
    public SocketThread(Socket clientSocket, Map<String, MyWebApplication> router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        try (Socket socket = clientSocket;
             BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream())) {
            byte[] buffer = new byte[2048];
            int n = bufferedInputStream.read(buffer);
            String rawRequest = new String(buffer, 0, n);

            Request request = new Request(rawRequest);
            System.out.println("Получен запрос.");
            request.show();

            boolean executed = false;
            for (Map.Entry<String, MyWebApplication> e : router.entrySet()) {
                if (request.getUri().startsWith(e.getKey())) {
                    e.getValue().execute(request, socket.getOutputStream());
                    executed = true;
                    break;
                }
            }
            if (!executed) {
                socket.getOutputStream().write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Unknown application</h1></body></html>").getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
