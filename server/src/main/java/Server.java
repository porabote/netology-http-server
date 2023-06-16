import handler.Handler;
import handler.HandlerCallable;
import request.Request;

import java.io.*;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {

    final static ExecutorService threadPool = Executors.newFixedThreadPool(64);
    final Map<String, Handler> handlersList = new HashMap<>();

    protected void listen(int port) {

        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                try (
                        final var socket = serverSocket.accept();
                ) {

                    final Request request = new Request(socket);
                    final var responseStream = new BufferedOutputStream(socket.getOutputStream());

                    HandlerCallable requestHandler = new HandlerCallable(request, responseStream, null);
                    String handlerKey = request.getHttpMethod() + ":" + request.getUri();
                    if (this.handlersList.containsKey(handlerKey)) {
                        requestHandler = new HandlerCallable(request, responseStream, this.handlersList.get(handlerKey));
                    }

                    final Future<String> task = threadPool.submit(requestHandler);
                    task.get();

                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addHandler(String httpMethod, String url, Handler handler) {
        this.handlersList.put(httpMethod + ":" + url, handler);
    }

}
