package org.mpkelly;

import com.sun.net.httpserver.HttpServer;
import org.mpkelly.handler.MatchingHttpHandler;
import org.mpkelly.handler.RootHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

public class WebServer {

  public static WebServer start(int port) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    RootHandler rootHandler = new RootHandler();
    server.createContext("/", rootHandler);
    server.start();

    waitForStart(port);

    rootHandler.clearState();

    return new WebServer(server, rootHandler);
  }

  // This sucks but there doesn't appear to any other way to wait until the server
  // can accept requests.
  private static void waitForStart(int port) throws IOException {
    while (true) {
      InputStream stream = null;
      try {
        URLConnection connection = new URL("http://localhost:" + port).openConnection();
        stream = connection.getInputStream();
      } catch (FileNotFoundException expected) {
        //404 = server online
        return;
      } finally {
        if (stream != null) {
          stream.close();
        }
      }
    }
  }

  private final HttpServer server;
  private final RootHandler rootHandler;

  private WebServer(HttpServer server, RootHandler rootHandler) {
    this.server = server;
    this.rootHandler = rootHandler;
  }

  public void addHandler(MatchingHttpHandler handler) {
    rootHandler.addHandler(handler);
  }

  public boolean allHandlersMatched() {
    return rootHandler.allHandlersMatched();
  }

  public void clearState() {
    rootHandler.clearState();
  }

  public void printState() {
    rootHandler.printState();
  }

  public void stop() {
    server.stop(0);
  }
}
