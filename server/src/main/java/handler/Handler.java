package handler;

import request.Request;
import response.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Handler implements IHandler {

    public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
        System.out.println(399);
        Response response = new Response(request, responseStream);
        response.output();
    }
}
