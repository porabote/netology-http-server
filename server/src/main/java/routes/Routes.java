package routes;

import java.util.List;

public class Routes {

    final public static List validPaths = List.of(
            "/index.html",
            "/messages",
            "/spring.svg",
            "/spring.png",
            "/resources.html",
            "/styles.css",
            "/app.js",
            "/links.html",
            "/forms.html",
            "/classic.html",
            "/events.html",
            "/events.js"
    );

    public static boolean contain(String path) {
        return validPaths.contains(path) ? true : false;
    }

}
