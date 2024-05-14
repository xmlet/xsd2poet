package org.xmlet.htmlapifaster;

import java.lang.String;
import java.util.List;

public class htmlapifaster {
  private String name;

  public String getName() {
    return this.name;
  }

  public String setName(String name) {
    this.name = name;
  }

  public static void sumOfTen(int number, List<String> strings) {
    int sum = 0;
    for (int i = 0; i <= 10; i++) {
      sum += i;
    }
  }
}
