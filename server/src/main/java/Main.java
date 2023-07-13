import request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.*;
import handler.Handler;
import response.Response;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Server server = new Server();

        // добавление хендлеров (обработчиков)
        server.addHandler("GET", "/messages", new Handler () {
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                Response response = new Response(request, responseStream);
                response.setTemplatePath("messages_override.html");
                response.output();
            }
        });
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                Response response = new Response(request, responseStream);
                response.setTemplatePath("messages_override_form.html");
                response.output();
            }
        });

        server.listen(9999);

    }

}
