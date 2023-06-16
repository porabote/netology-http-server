package handler;

import request.Request;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface IHandler {
    public Object handle(Request request, BufferedOutputStream responseStream) throws IOException;
}
