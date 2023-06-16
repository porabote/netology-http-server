package response;

import request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Response {
    private Request request;
    private int code = 200;
    private String headers;
    private String body;
    private String mimeType;
    private BufferedOutputStream responseStream;
    private String templatePath;

    public Response(Request request, BufferedOutputStream responseStream) throws IOException {

        this.request = request;
        this.responseStream = responseStream;
        this.templatePath = request.getUri();
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public void output() throws IOException {

        final var filePath = Path.of(".", "public", this.templatePath);

        this.mimeType = Files.probeContentType(filePath);

        this.body = Files.readString(filePath);

        final var content = this.body.getBytes();
        this.responseStream.write((
                "HTTP/1.1 " + this.code + " OK\r\n" +
                        "Content-Type: " + this.mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        this.responseStream.write(content);
        this.responseStream.flush();

    }
}
