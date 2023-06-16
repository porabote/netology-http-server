package handler;

import request.Request;

import java.io.BufferedOutputStream;
import java.util.concurrent.Callable;

public class HandlerCallable implements Callable {

    private Request request;
    private BufferedOutputStream responseStream;
    private Handler customHandler;

    public HandlerCallable(Request request, BufferedOutputStream responseStream, Handler customHandler) {
        this.request = request;
        this.responseStream = responseStream;
        this.customHandler = customHandler;
    }

    @Override
    public Object call() throws Exception {
        if (this.customHandler == null) {
            return new Handler().handle(this.request, this.responseStream);
        } else {
            return this.customHandler.handle(this.request, this.responseStream);
        }
    }

}
