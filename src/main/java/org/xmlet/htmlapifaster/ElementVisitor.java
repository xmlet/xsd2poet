package org.xmlet.htmlapifaster;

import org.xmlet.htmlapifaster.async.AwaitConsumer;

import java.util.function.BiConsumer;

public abstract class ElementVisitor {
  public ElementVisitor() {
  }

  public abstract void visitElement(Element var1);

  public abstract void visitAttribute(String var1, String var2);

  public abstract void visitAttributeBoolean(String var1, String var2);

  public abstract void visitParent(Element var1);

  public abstract <R> void visitText(Text<? extends Element, R> var1);

  public abstract <R> void visitRaw(Text<? extends Element, R> var1);

  public abstract <R> void visitComment(Text<? extends Element, R> var1);

  public abstract <E extends Element, U> void visitDynamic(E var1, BiConsumer<E, U> var2);

  public abstract <M, E extends Element> void visitAwait(E var1, AwaitConsumer<E, M> var2);

  public abstract <M, E extends Element> void visitSuspending(E var1, SuspendConsumer<E, M> var2);
}

