package org.mpkelly.util;

import java.io.InputStream;
import java.util.Scanner;

public final class Streams {

  public static String readInputStream(InputStream stream, String charset) {
    Scanner scanner = new Scanner(stream, charset).useDelimiter("\\A");
    return scanner.hasNext() ? scanner.next() : "";
  }
}
