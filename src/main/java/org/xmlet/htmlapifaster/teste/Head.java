package org.xmlet.htmlapifaster.teste;

import java.lang.String;
import org.xmlet.htmlapifaster.Element;

public final class Head<Z extends Element> {
  protected final ElementVisitor visitor;

  protected final Z parent;

  public Head(ElementVisitor visitor) {
    this.visitor = visitor;
    this.parent = null;
    visitor.visitElementHead(this);
  }

  public Head(Z parent) {
    this.parent = parent;
    this.visitor = parent.getVisitor();
    this.visitor.visitElementHead(this);
  }

  protected Head(Z parent, ElementVisitor visitor, boolean shouldVisit) {
    this.parent = parent;
    this.visitor = visitor;
    if (shouldVisit) {
      visitor.visitElementHead(this);
    }
  }

  public Z __() {
    this.visitor.visitParentElementHead(this);
    return this.parent;
  }

  public Z getParent() {
    return this.parent;
  }

  public final ElementVisitor getVisitor() {
    return this.visitor;
  }

  public String getName() {
    return "head";
  }

  public final Head<Z> self() {
    return this;
  }
}
