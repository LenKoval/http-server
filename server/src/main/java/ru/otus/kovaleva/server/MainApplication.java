package ru.otus.kovaleva.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainApplication {
    public static final int PORT = 8189; // положить в конф файл

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // слушает порт
            Map<String, MyWebApplication> router = new HashMap<>();
            // /calculator
            // /greetings
            router.put("/calculator", new CalculatorWebApplication());
            router.put("/greetings", new GreetingsWebApplication());
            System.out.println("Сервер запущен порт: " + PORT); // подключить логгер
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Клиент подключился.");
                    new SocketThread(socket, router).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// GET / HTTP/1.1 http-метод(get post put delete), путь, версия протокола
// заголовки
// Host: localhost:8189 адрес
// Connection: keep-alive мб мы пошлем неск запросов, держит соединение
// sec-ch-ua: "Microsoft Edge";v="119", "Chromium";v="119", "Not?A_Brand";v="24"
// sec-ch-ua-mobile: ?0
// sec-ch-ua-platform: "Windows"
// Upgrade-Insecure-Requests: 1
// User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0 браузер себя помечает
// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7 клиент ожидает ответ от сервера
// Sec-Fetch-Site: none
// Sec-Fetch-Mode: navigate
// Sec-Fetch-User: ?1
// Sec-Fetch-Dest: document
// Accept-Encoding: gzip, deflate, br сжаттие
// Accept-Language: ru,en;q=0.9,en-GB;q=0.8,en-US;q=0.7 язык

// http-ответ
// HTTP/1.1 протокол 200 статус-код OK расшифровка статус-кода (значит что запрос был успешно обработан)
//Content-Type: заголовки text/html в ответе только htmlстраничка
// пустая строка
//<html>
//  <body>
//    <h1>Hello World</h1> тело ответа (в запросе мб тело)
//  </body>
//</html>
