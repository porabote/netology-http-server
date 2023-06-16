package handler;

import request.Request;
import response.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class Handler implements IHandler {

    public Object handle(Request request, BufferedOutputStream responseStream) throws IOException {

        Response response = new Response(request, responseStream);
        response.output();

        return null;
    }
}
