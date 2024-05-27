package org.xmlet.htmlapifaster.teste;

import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.TextGroup;

public interface HtmlChoice<T extends Element<T, Z>, Z extends Element> extends TextGroup<T, Z> {
  default Body<T> body() {
    return new Body(this.self());
  }

  default Head<T> head() {
    return new Head(this.self());
  }
}
