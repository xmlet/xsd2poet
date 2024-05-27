package org.xmlet.htmlapifaster.teste;

import java.lang.String;
import org.xmlet.htmlapifaster.Element;

public final class Title<Z extends Element> {
  protected final ElementVisitor visitor;

  protected final Z parent;

  public Title(ElementVisitor visitor) {
    this.visitor = visitor;
    this.parent = null;
    visitor.visitElementTitle(this);
  }

  public Title(Z parent) {
    this.parent = parent;
    this.visitor = parent.getVisitor();
    this.visitor.visitElementTitle(this);
  }

  protected Title(Z parent, ElementVisitor visitor, boolean shouldVisit) {
    this.parent = parent;
    this.visitor = visitor;
    if (shouldVisit) {
      visitor.visitElementTitle(this);
    }
  }

  public Z __() {
    this.visitor.visitParentElementTitle(this);
    return this.parent;
  }

  public Z getParent() {
    return this.parent;
  }

  public final ElementVisitor getVisitor() {
    return this.visitor;
  }

  public String getName() {
    return "title";
  }

  public final Title<Z> self() {
    return this;
  }
}
