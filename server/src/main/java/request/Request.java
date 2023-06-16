package request;

import routes.Routes;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Request {

    private Socket socket;
    private String httpMethod;
    private String uri;
    private String protocolType;
    private String body;
    private BufferedOutputStream out;


    public Request(Socket socket) throws IOException {
        this.socket = socket;
        final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.parseRequest(in.readLine());
    }

    public String getUri() {
        return this.uri;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public String getProtocolType() {
        return this.protocolType;
    }

    public String getBody() {
        return this.body;
    }
    
    private void parseRequest(String requestLine) throws IOException {

        this.out = new BufferedOutputStream(socket.getOutputStream());

        final var parts = requestLine.split(" ");
        this.httpMethod = parts[0];
        this.uri = parts[1];
        this.protocolType = parts[2];

        if (parts.length != 3) {
            throw new IOException("Missed request part");
        }

        if (!Routes.contain(this.uri)) {
            this.error404();
        }
    }

    public void error404() throws IOException {
        this.out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        this.out.flush();
    }
}
