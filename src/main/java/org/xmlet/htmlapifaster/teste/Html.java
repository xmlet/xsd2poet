package org.xmlet.htmlapifaster.teste;

import java.lang.String;
import org.xmlet.htmlapifaster.Element;

public final class Html<Z extends Element> {
  protected final ElementVisitor visitor;

  protected final Z parent;

  public Html(ElementVisitor visitor) {
    this.visitor = visitor;
    this.parent = null;
    visitor.visitElementHtml(this);
  }

  public Html(Z parent) {
    this.parent = parent;
    this.visitor = parent.getVisitor();
    this.visitor.visitElementHtml(this);
  }

  protected Html(Z parent, ElementVisitor visitor, boolean shouldVisit) {
    this.parent = parent;
    this.visitor = visitor;
    if (shouldVisit) {
      visitor.visitElementHtml(this);
    }
  }

  public Z __() {
    this.visitor.visitParentElementHtml(this);
    return this.parent;
  }

  public Z getParent() {
    return this.parent;
  }

  public final ElementVisitor getVisitor() {
    return this.visitor;
  }

  public String getName() {
    return "html";
  }

  public final Html<Z> self() {
    return this;
  }
}
