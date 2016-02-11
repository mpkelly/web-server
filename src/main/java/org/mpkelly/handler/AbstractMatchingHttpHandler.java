package org.mpkelly.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;
import static org.mpkelly.handler.RootHandler.DEFAULT_CHARSET;
import static org.mpkelly.util.Streams.readInputStream;

public abstract class AbstractMatchingHttpHandler implements MatchingHttpHandler {

  protected final String path;
  protected final String method;
  protected final String body;
  protected final Map<String, List<String>> headers;

  protected AbstractMatchingHttpHandler(String path, String method, String body, Map<String, List<String>> headers) {
    this.path = path;
    this.method = method;
    this.body = body;
    this.headers = headers;
  }

  @Override public boolean matches(HttpExchange exchange) {
    String path = exchange.getRequestURI().toString();
    String method = exchange.getRequestMethod();
    Headers headers = exchange.getRequestHeaders();

    String charset = DEFAULT_CHARSET;

    if(headers.containsKey("charset")) {
      charset = headers.getFirst("charset");
    }

    String body = readInputStream(exchange.getRequestBody(), charset );
    return matches(path, method, body, headers);
  }

  public int returnCode() {
    return 200;
  }

  public Map<String, List<String>> responseHeaders() {
    return new HashMap<>();
  }

  public abstract boolean matches(String path, String method, String body, Headers headers);

  public abstract String responseBody();

  @Override public String toString() {
    return format("Path: {0}\nMethod: {1}\nHeaders: {2}\nBody: {3} ", path, method, headers, body);
  }
}
