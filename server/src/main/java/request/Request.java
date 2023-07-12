package request;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import routes.Routes;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.http.message.BasicHeaderValueFormatter.SEPARATORS;


public class Request {

    private Socket socket;
    private String httpMethod;
    private String uri;
    private String protocolType;
    private String body;
    private BufferedOutputStream out;
    private List <NameValuePair> queryParams = new ArrayList<>();


    public Request(Socket socket) throws IOException, URISyntaxException {
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
    
    private void parseRequest(String requestLine) throws IOException, URISyntaxException {

        this.out = new BufferedOutputStream(socket.getOutputStream());

        final var parts = requestLine.split(" ");
        this.httpMethod = parts[0];
        String url = parts[1];
        this.protocolType = parts[2];

        String[] uriSplits = url.split("\\?");

        this.uri = uriSplits[0];

        if (uriSplits.length > 1) {
            this.queryParams = URLEncodedUtils.parse(uriSplits[1], StandardCharsets.UTF_8);
        }

        String fooValue = this.getQueryParam("bar");
        System.out.println(fooValue);

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

    public String getQueryParam(String property) {

        String propertyValue = null;

        if (this.uri != null && !this.uri.isEmpty()) {
            for (NameValuePair param : this.queryParams) {
                if(property.equals(param.getName())){
                    propertyValue = param.getValue();
                    break;
                }
            }
        }
        return propertyValue;
    }

    public List <NameValuePair> getQueryParams() {
        return this.queryParams;
    }
}
