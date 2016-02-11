package org.mpkelly.tests;

import org.junit.After;
import org.junit.Before;
import org.mpkelly.WebServer;

import java.io.IOException;

import static junit.framework.Assert.fail;

public class AbstractWebServerTest {

  protected WebServer server;

  @Before public void createWebServer() throws IOException {
    server = WebServer.start(9999);
  }

  @After public void stopServer() {
    server.stop();
  }

  @After public void checkServer() {
    if (!server.allHandlersMatched()) {
      server.printState();
      fail("Server received unexpected requests or did not match all handlers");
    }
  }
}
