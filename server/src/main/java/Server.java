import handler.Handler;
import handler.HandlerRunnable;
import request.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.URISyntaxException;
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

                final var socket = serverSocket.accept();

                final Request request = new Request(socket);
                final var responseStream = new BufferedOutputStream(socket.getOutputStream());

                HandlerRunnable requestHandler;

                String handlerKey = request.getHttpMethod() + ":" + request.getUri();

                if (this.handlersList.containsKey(handlerKey)) {
                    requestHandler = new HandlerRunnable(request, responseStream, this.handlersList.get(handlerKey));
                } else {
                    requestHandler = new HandlerRunnable(request, responseStream, null);
                }
                Thread responseHandle = new Thread(requestHandler);
                threadPool.execute(responseHandle);
                //threadPool.shutdown();
//                responseHandle.join();
//                if (!socket.isClosed()) {
//                    System.out.println("CLOSED");
//                }

//            } catch(URISyntaxException e){
//                throw new RuntimeException(e);
//            }
//            catch(InterruptedException e){
//                throw new RuntimeException(e);
//            }
        }
    } catch(
    IOException e)

    {
        e.printStackTrace();
    } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    protected void addHandler(String httpMethod, String url, Handler handler) {
        this.handlersList.put(httpMethod + ":" + url, handler);
    }

}
