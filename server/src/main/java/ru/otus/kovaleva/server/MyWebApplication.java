package ru.otus.kovaleva.server;

import java.io.IOException;
import java.io.OutputStream;

public interface MyWebApplication {
    void execute(Request request, OutputStream outputStream) throws IOException;
}
