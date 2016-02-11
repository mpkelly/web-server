package org.mpkelly;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;

public class Request {
  public static final String MATCHED = "MATCHED";
  public static final String UNMATCHED = "UNMATCHED";

  private final String status;
  private final String path;
  private final Map<String, List<String>> headers = new HashMap<>();
  private final String method;
  private final String body;

  public Request(String status, String path, Map<String, List<String>> headers, String method, String body) {
    this.status = status;
    this.path = path;
    this.headers.putAll(headers);
    this.method = method;
    this.body = body;
  }

  @Override public String toString() {
    return format("Path: {0}\nMethod: {1}\nStatus: {2}\nHeaders: {3}\nBody: {4} ", path, method, status, headers, body);
  }

  public static Request matched(HttpExchange exchange) {
    return request( exchange, MATCHED);
  }

  public static Request unmatched(HttpExchange exchange) {
    return request( exchange, UNMATCHED);
  }

  public static Request request(HttpExchange exchange, String status) {
    return new Request(status, exchange.getRequestURI().toString(), exchange.getRequestHeaders(), exchange.getRequestMethod(), "");
  }
}
