package handler;

import request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

public class HandlerRunnable implements Runnable {

    private Request request;
    private BufferedOutputStream responseStream;
    private Handler customHandler;

    public HandlerRunnable(Request request, BufferedOutputStream responseStream, Handler customHandler) {
        this.request = request;
        this.responseStream = responseStream;
        this.customHandler = customHandler;
    }

    @Override
    public void run() {
        if (this.customHandler == null) {
            try {
                new Handler().handle(this.request, this.responseStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                this.customHandler.handle(this.request, this.responseStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
