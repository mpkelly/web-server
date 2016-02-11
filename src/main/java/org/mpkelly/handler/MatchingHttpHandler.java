package org.mpkelly.handler;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Map;

public interface MatchingHttpHandler {

  boolean matches(HttpExchange exchange);

  int returnCode();

  String responseBody();

  Map<String, List<String>> responseHeaders();
}
