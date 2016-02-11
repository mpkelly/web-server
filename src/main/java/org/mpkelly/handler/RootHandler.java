package org.mpkelly.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.mpkelly.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootHandler implements HttpHandler {
  public static final String DEFAULT_CHARSET = "utf-8";

  final List<MatchingHttpHandler> handlers = new ArrayList<>();
  final List<Request> matchedHandlers = new ArrayList<>();
  final List<Request> unexpectedRequests = new ArrayList<>();

  public void addHandler(MatchingHttpHandler handler) {
    handlers.add(handler);
  }

  @Override public void handle(HttpExchange exchange) throws IOException {
    MatchingHttpHandler handler = find(exchange);

    if (handler == null) {
      unexpectedRequests.add(Request.unmatched(exchange));
      resourceNotFound(exchange);
      return;
    }

    matchedHandlers.add(Request.matched(exchange));
    handlers.remove(handler);

    writeResponse(exchange, handler.returnCode(), handler.responseBody(), handler.responseHeaders());
  }

  private void resourceNotFound(HttpExchange httpExchange) throws IOException {
    writeResponse(httpExchange, 404, "No handler set for " + httpExchange.getRequestURI(), new HashMap<String, List<String>>());
  }

  protected void writeResponse(HttpExchange httpExchange, int code, String content, Map<String, List<String>> headers) throws IOException {
    httpExchange.sendResponseHeaders(code, content.length());
    httpExchange.getResponseHeaders().putAll(headers);
    try (OutputStream os = httpExchange.getResponseBody()) {
      os.write(content.getBytes());
    }
  }

  protected MatchingHttpHandler find(HttpExchange exchange) {
    for (MatchingHttpHandler handler : handlers) {
      if (handler.matches(exchange)) {
        return handler;
      }
    }
    return null;
  }

  public boolean allHandlersMatched() {
    return handlers.isEmpty() && unexpectedRequests.isEmpty();
  }

  public void printState() {
    StringBuilder unexpected = new StringBuilder("Unexpected Requests");
    appendLines(unexpectedRequests, unexpected);

    StringBuilder unmatched = new StringBuilder("Unmatched Handlers");
    appendLines(handlers, unmatched);

    StringBuilder matched = new StringBuilder("Matched Handlers");
    appendLines(matchedHandlers, matched);

    System.out.println(unexpected.toString() + unmatched + matched);
  }

  private StringBuilder appendLines(List<?> lines, StringBuilder builder) {
    String newline = "\n";
    builder.append(newline);

    if(lines.isEmpty()) {
      builder.append("<NONE>");
    }

    builder.append(newline);

    for(Object request : lines) {
      builder.append(request);
      builder.append(newline);
    }

    builder.append(newline);

    return builder;
  }

  public void clearState() {
    handlers.clear();
    unexpectedRequests.clear();
  }
}
