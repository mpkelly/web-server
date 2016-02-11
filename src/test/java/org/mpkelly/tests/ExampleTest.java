package org.mpkelly.tests;

import com.sun.net.httpserver.Headers;
import org.junit.Test;
import org.mpkelly.handler.AbstractMatchingHttpHandler;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mpkelly.handler.RootHandler.DEFAULT_CHARSET;
import static org.mpkelly.util.Streams.readInputStream;

public class ExampleTest extends AbstractWebServerTest {

  @Test public void example_test() throws Exception {
    server.addHandler(new AbstractMatchingHttpHandler("/test", "GET", "", new HashMap<String, List<String>>()) {

      @Override public boolean matches(String path, String method, String body, Headers headers) {
        return this.path.equals(path);
      }

      @Override public String responseBody() {
        return "success";
      }
    });

    // Used an example only - you want to be using a Http Client library
    URLConnection connection = new URL("http://127.0.0.1:9999/test").openConnection();
    connection.setRequestProperty("Accept-Charset", DEFAULT_CHARSET);
    String response = readInputStream(connection.getInputStream(), DEFAULT_CHARSET);

    assertEquals("response", "success", response);
  }
}
