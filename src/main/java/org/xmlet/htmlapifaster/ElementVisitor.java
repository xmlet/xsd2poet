package org.xmlet.htmlapifaster;

import java.lang.String;
import java.util.function.BiConsumer;
import org.xmlet.htmlapifaster.async.AwaitConsumer;

public abstract class ElementVisitor {
  public ElementVisitor() {
  }

  public abstract void visitElement(Element element);

  public abstract void visitAttribute(String var1, String var2);

  public abstract void visitAttributeBoolean(String var1, String var2);

  public abstract void visitParent(Element element);

  public abstract <R> void visitText(Text<? extends Element, R> var1);

  public abstract <R> void visitRaw(Text<? extends Element, R> var1);

  public abstract <R> void visitComment(Text<? extends Element, R> var1);

  public abstract <E extends Element, U> void visitDynamic(E var1, BiConsumer<E, U> var2);

  public abstract <M, E extends Element> void visitAwait(E var1, AwaitConsumer<E, M> var2);

  public abstract <M, E extends Element> void visitSuspending(E var1, SuspendConsumer<E, M> var2);

  public <Z extends Element> void visitElementHtml(Html<Z> html) {
    this.visitElement(html);
  }

  public <Z extends Element> void visitParentHtml(Html<Z> html) {
    this.visitParent(html);
  }

  public void visitAttributeManifest(String manifest) {
    this.visitAttribute("manifest", manifest);
  }

  public <Z extends Element> void visitElementHead(Head<Z> head) {
    this.visitElement(head);
  }

  public <Z extends Element> void visitParentHead(Head<Z> head) {
    this.visitParent(head);
  }

  public <Z extends Element> void visitElementTitle(Title<Z> title) {
    this.visitElement(title);
  }

  public <Z extends Element> void visitParentTitle(Title<Z> title) {
    this.visitParent(title);
  }

  public <Z extends Element> void visitElementBase(Base<Z> base) {
    this.visitElement(base);
  }

  public <Z extends Element> void visitParentBase(Base<Z> base) {
    this.visitParent(base);
  }

  public void visitAttributeHref(String href) {
    this.visitAttribute("href", href);
  }

  public void visitAttributeTarget(String target) {
    this.visitAttribute("target", target);
  }

  public <Z extends Element> void visitElementLink(Link<Z> link) {
    this.visitElement(link);
  }

  public <Z extends Element> void visitParentLink(Link<Z> link) {
    this.visitParent(link);
  }

  public void visitAttributeCrossorigin(String crossorigin) {
    this.visitAttribute("crossorigin", crossorigin);
  }

  public void visitAttributeRel(String rel) {
    this.visitAttribute("rel", rel);
  }

  public void visitAttributeRev(String rev) {
    this.visitAttribute("rev", rev);
  }

  public void visitAttributeMedia(String media) {
    this.visitAttribute("media", media);
  }

  public void visitAttributeNonce(String nonce) {
    this.visitAttribute("nonce", nonce);
  }

  public void visitAttributeHreflang(String hreflang) {
    this.visitAttribute("hreflang", hreflang);
  }

  public void visitAttributeType(String type) {
    this.visitAttribute("type", type);
  }

  public void visitAttributeReferrerpolicy(String referrerpolicy) {
    this.visitAttribute("referrerpolicy", referrerpolicy);
  }

  public void visitAttributeSizes(String sizes) {
    this.visitAttribute("sizes", sizes);
  }

  public <Z extends Element> void visitElementMeta(Meta<Z> meta) {
    this.visitElement(meta);
  }

  public <Z extends Element> void visitParentMeta(Meta<Z> meta) {
    this.visitParent(meta);
  }

  public void visitAttributeCharset(String charset) {
    this.visitAttribute("charset", charset);
  }

  public void visitAttributeContent(String content) {
    this.visitAttribute("content", content);
  }

  public void visitAttributeHttpEquiv(String httpEquiv) {
    this.visitAttribute("http-equiv", httpEquiv);
  }

  public void visitAttributeName(String name) {
    this.visitAttribute("name", name);
  }

  public <Z extends Element> void visitElementStyle(Style<Z> style) {
    this.visitElement(style);
  }

  public <Z extends Element> void visitParentStyle(Style<Z> style) {
    this.visitParent(style);
  }

  public <Z extends Element> void visitElementScript(Script<Z> script) {
    this.visitElement(script);
  }

  public <Z extends Element> void visitParentScript(Script<Z> script) {
    this.visitParent(script);
  }

  public void visitAttributeSrc(String src) {
    this.visitAttribute("src", src);
  }

  public void visitAttributeAsync(String async) {
    this.visitAttributeBoolean("async", async);
  }

  public void visitAttributeDefer(String defer) {
    this.visitAttributeBoolean("defer", defer);
  }

  public <Z extends Element> void visitElementNoscript(Noscript<Z> noscript) {
    this.visitElement(noscript);
  }

  public <Z extends Element> void visitParentNoscript(Noscript<Z> noscript) {
    this.visitParent(noscript);
  }

  public <Z extends Element> void visitElementBody(Body<Z> body) {
    this.visitElement(body);
  }

  public <Z extends Element> void visitParentBody(Body<Z> body) {
    this.visitParent(body);
  }

  public void visitAttributeOnafterprint(String onafterprint) {
    this.visitAttribute("onafterprint", onafterprint);
  }

  public void visitAttributeOnbeforeprint(String onbeforeprint) {
    this.visitAttribute("onbeforeprint", onbeforeprint);
  }

  public void visitAttributeOnbeforeunload(String onbeforeunload) {
    this.visitAttribute("onbeforeunload", onbeforeunload);
  }

  public void visitAttributeOnhashchange(String onhashchange) {
    this.visitAttribute("onhashchange", onhashchange);
  }

  public void visitAttributeOnlanguagechange(String onlanguagechange) {
    this.visitAttribute("onlanguagechange", onlanguagechange);
  }

  public void visitAttributeOnmessage(String onmessage) {
    this.visitAttribute("onmessage", onmessage);
  }

  public void visitAttributeOnoffline(String onoffline) {
    this.visitAttribute("onoffline", onoffline);
  }

  public void visitAttributeOnonline(String ononline) {
    this.visitAttribute("ononline", ononline);
  }

  public void visitAttributeOnpagehide(String onpagehide) {
    this.visitAttribute("onpagehide", onpagehide);
  }

  public void visitAttributeOnpageshow(String onpageshow) {
    this.visitAttribute("onpageshow", onpageshow);
  }

  public void visitAttributeOnpopstate(String onpopstate) {
    this.visitAttribute("onpopstate", onpopstate);
  }

  public void visitAttributeOnrejectionhandled(String onrejectionhandled) {
    this.visitAttribute("onrejectionhandled", onrejectionhandled);
  }

  public void visitAttributeOnstorage(String onstorage) {
    this.visitAttribute("onstorage", onstorage);
  }

  public void visitAttributeOnunhandledrejection(String onunhandledrejection) {
    this.visitAttribute("onunhandledrejection", onunhandledrejection);
  }

  public void visitAttributeOnunload(String onunload) {
    this.visitAttribute("onunload", onunload);
  }

  public <Z extends Element> void visitElementSection(Section<Z> section) {
    this.visitElement(section);
  }

  public <Z extends Element> void visitParentSection(Section<Z> section) {
    this.visitParent(section);
  }

  public <Z extends Element> void visitElementNav(Nav<Z> nav) {
    this.visitElement(nav);
  }

  public <Z extends Element> void visitParentNav(Nav<Z> nav) {
    this.visitParent(nav);
  }

  public <Z extends Element> void visitElementArticle(Article<Z> article) {
    this.visitElement(article);
  }

  public <Z extends Element> void visitParentArticle(Article<Z> article) {
    this.visitParent(article);
  }

  public <Z extends Element> void visitElementAside(Aside<Z> aside) {
    this.visitElement(aside);
  }

  public <Z extends Element> void visitParentAside(Aside<Z> aside) {
    this.visitParent(aside);
  }

  public <Z extends Element> void visitElementH1(H1<Z> h1) {
    this.visitElement(h1);
  }

  public <Z extends Element> void visitParentH1(H1<Z> h1) {
    this.visitParent(h1);
  }

  public <Z extends Element> void visitElementH2(H2<Z> h2) {
    this.visitElement(h2);
  }

  public <Z extends Element> void visitParentH2(H2<Z> h2) {
    this.visitParent(h2);
  }

  public <Z extends Element> void visitElementH3(H3<Z> h3) {
    this.visitElement(h3);
  }

  public <Z extends Element> void visitParentH3(H3<Z> h3) {
    this.visitParent(h3);
  }

  public <Z extends Element> void visitElementH4(H4<Z> h4) {
    this.visitElement(h4);
  }

  public <Z extends Element> void visitParentH4(H4<Z> h4) {
    this.visitParent(h4);
  }

  public <Z extends Element> void visitElementH5(H5<Z> h5) {
    this.visitElement(h5);
  }

  public <Z extends Element> void visitParentH5(H5<Z> h5) {
    this.visitParent(h5);
  }

  public <Z extends Element> void visitElementH6(H6<Z> h6) {
    this.visitElement(h6);
  }

  public <Z extends Element> void visitParentH6(H6<Z> h6) {
    this.visitParent(h6);
  }

  public <Z extends Element> void visitElementHeader(Header<Z> header) {
    this.visitElement(header);
  }

  public <Z extends Element> void visitParentHeader(Header<Z> header) {
    this.visitParent(header);
  }

  public <Z extends Element> void visitElementFooter(Footer<Z> footer) {
    this.visitElement(footer);
  }

  public <Z extends Element> void visitParentFooter(Footer<Z> footer) {
    this.visitParent(footer);
  }

  public <Z extends Element> void visitElementAddress(Address<Z> address) {
    this.visitElement(address);
  }

  public <Z extends Element> void visitParentAddress(Address<Z> address) {
    this.visitParent(address);
  }

  public <Z extends Element> void visitElementP(P<Z> p) {
    this.visitElement(p);
  }

  public <Z extends Element> void visitParentP(P<Z> p) {
    this.visitParent(p);
  }

  public <Z extends Element> void visitElementBr(Br<Z> br) {
    this.visitElement(br);
  }

  public <Z extends Element> void visitParentBr(Br<Z> br) {
    this.visitParent(br);
  }

  public <Z extends Element> void visitElementPre(Pre<Z> pre) {
    this.visitElement(pre);
  }

  public <Z extends Element> void visitParentPre(Pre<Z> pre) {
    this.visitParent(pre);
  }

  public <Z extends Element> void visitElementDialog(Dialog<Z> dialog) {
    this.visitElement(dialog);
  }

  public <Z extends Element> void visitParentDialog(Dialog<Z> dialog) {
    this.visitParent(dialog);
  }

  public void visitAttributeOpen(String open) {
    this.visitAttributeBoolean("open", open);
  }

  public <Z extends Element> void visitElementBlockquote(Blockquote<Z> blockquote) {
    this.visitElement(blockquote);
  }

  public <Z extends Element> void visitParentBlockquote(Blockquote<Z> blockquote) {
    this.visitParent(blockquote);
  }

  public void visitAttributeCite(String cite) {
    this.visitAttribute("cite", cite);
  }

  public <Z extends Element> void visitElementOl(Ol<Z> ol) {
    this.visitElement(ol);
  }

  public <Z extends Element> void visitParentOl(Ol<Z> ol) {
    this.visitParent(ol);
  }

  public void visitAttributeStart(String start) {
    this.visitAttribute("start", start);
  }

  public void visitAttributeReversed(String reversed) {
    this.visitAttributeBoolean("reversed", reversed);
  }

  public <Z extends Element> void visitElementUl(Ul<Z> ul) {
    this.visitElement(ul);
  }

  public <Z extends Element> void visitParentUl(Ul<Z> ul) {
    this.visitParent(ul);
  }

  public <Z extends Element> void visitElementLi(Li<Z> li) {
    this.visitElement(li);
  }

  public <Z extends Element> void visitParentLi(Li<Z> li) {
    this.visitParent(li);
  }

  public void visitAttributeValue(String value) {
    this.visitAttribute("value", value);
  }

  public <Z extends Element> void visitElementDl(Dl<Z> dl) {
    this.visitElement(dl);
  }

  public <Z extends Element> void visitParentDl(Dl<Z> dl) {
    this.visitParent(dl);
  }

  public <Z extends Element> void visitElementDt(Dt<Z> dt) {
    this.visitElement(dt);
  }

  public <Z extends Element> void visitParentDt(Dt<Z> dt) {
    this.visitParent(dt);
  }

  public <Z extends Element> void visitElementDd(Dd<Z> dd) {
    this.visitElement(dd);
  }

  public <Z extends Element> void visitParentDd(Dd<Z> dd) {
    this.visitParent(dd);
  }

  public <Z extends Element> void visitElementA(A<Z> a) {
    this.visitElement(a);
  }

  public <Z extends Element> void visitParentA(A<Z> a) {
    this.visitParent(a);
  }

  public void visitAttributeDownload(String download) {
    this.visitAttribute("download", download);
  }

  public <Z extends Element> void visitElementEm(Em<Z> em) {
    this.visitElement(em);
  }

  public <Z extends Element> void visitParentEm(Em<Z> em) {
    this.visitParent(em);
  }

  public <Z extends Element> void visitElementStrong(Strong<Z> strong) {
    this.visitElement(strong);
  }

  public <Z extends Element> void visitParentStrong(Strong<Z> strong) {
    this.visitParent(strong);
  }

  public <Z extends Element> void visitElementSmall(Small<Z> small) {
    this.visitElement(small);
  }

  public <Z extends Element> void visitParentSmall(Small<Z> small) {
    this.visitParent(small);
  }

  public <Z extends Element> void visitElementCite(Cite<Z> cite) {
    this.visitElement(cite);
  }

  public <Z extends Element> void visitParentCite(Cite<Z> cite) {
    this.visitParent(cite);
  }

  public <Z extends Element> void visitElementQ(Q<Z> q) {
    this.visitElement(q);
  }

  public <Z extends Element> void visitParentQ(Q<Z> q) {
    this.visitParent(q);
  }

  public <Z extends Element> void visitElementDfn(Dfn<Z> dfn) {
    this.visitElement(dfn);
  }

  public <Z extends Element> void visitParentDfn(Dfn<Z> dfn) {
    this.visitParent(dfn);
  }

  public <Z extends Element> void visitElementAbbr(Abbr<Z> abbr) {
    this.visitElement(abbr);
  }

  public <Z extends Element> void visitParentAbbr(Abbr<Z> abbr) {
    this.visitParent(abbr);
  }

  public <Z extends Element> void visitElementCode(Code<Z> code) {
    this.visitElement(code);
  }

  public <Z extends Element> void visitParentCode(Code<Z> code) {
    this.visitParent(code);
  }

  public <Z extends Element> void visitElementVar(Var<Z> var) {
    this.visitElement(var);
  }

  public <Z extends Element> void visitParentVar(Var<Z> var) {
    this.visitParent(var);
  }

  public <Z extends Element> void visitElementSamp(Samp<Z> samp) {
    this.visitElement(samp);
  }

  public <Z extends Element> void visitParentSamp(Samp<Z> samp) {
    this.visitParent(samp);
  }

  public <Z extends Element> void visitElementKbd(Kbd<Z> kbd) {
    this.visitElement(kbd);
  }

  public <Z extends Element> void visitParentKbd(Kbd<Z> kbd) {
    this.visitParent(kbd);
  }

  public <Z extends Element> void visitElementSub(Sub<Z> sub) {
    this.visitElement(sub);
  }

  public <Z extends Element> void visitParentSub(Sub<Z> sub) {
    this.visitParent(sub);
  }

  public <Z extends Element> void visitElementSup(Sup<Z> sup) {
    this.visitElement(sup);
  }

  public <Z extends Element> void visitParentSup(Sup<Z> sup) {
    this.visitParent(sup);
  }

  public <Z extends Element> void visitElementI(I<Z> i) {
    this.visitElement(i);
  }

  public <Z extends Element> void visitParentI(I<Z> i) {
    this.visitParent(i);
  }

  public <Z extends Element> void visitElementB(B<Z> b) {
    this.visitElement(b);
  }

  public <Z extends Element> void visitParentB(B<Z> b) {
    this.visitParent(b);
  }

  public <Z extends Element> void visitElementMark(Mark<Z> mark) {
    this.visitElement(mark);
  }

  public <Z extends Element> void visitParentMark(Mark<Z> mark) {
    this.visitParent(mark);
  }

  public <Z extends Element> void visitElementProgress(Progress<Z> progress) {
    this.visitElement(progress);
  }

  public <Z extends Element> void visitParentProgress(Progress<Z> progress) {
    this.visitParent(progress);
  }

  public void visitAttributeMax(String max) {
    this.visitAttribute("max", max);
  }

  public <Z extends Element> void visitElementMeter(Meter<Z> meter) {
    this.visitElement(meter);
  }

  public <Z extends Element> void visitParentMeter(Meter<Z> meter) {
    this.visitParent(meter);
  }

  public void visitAttributeMin(String min) {
    this.visitAttribute("min", min);
  }

  public void visitAttributeLow(String low) {
    this.visitAttribute("low", low);
  }

  public void visitAttributeHigh(String high) {
    this.visitAttribute("high", high);
  }

  public void visitAttributeOptimum(String optimum) {
    this.visitAttribute("optimum", optimum);
  }

  public <Z extends Element> void visitElementTime(Time<Z> time) {
    this.visitElement(time);
  }

  public <Z extends Element> void visitParentTime(Time<Z> time) {
    this.visitParent(time);
  }

  public void visitAttributeDatetime(String datetime) {
    this.visitAttribute("datetime", datetime);
  }

  public <Z extends Element> void visitElementRuby(Ruby<Z> ruby) {
    this.visitElement(ruby);
  }

  public <Z extends Element> void visitParentRuby(Ruby<Z> ruby) {
    this.visitParent(ruby);
  }

  public <Z extends Element> void visitElementRt(Rt<Z> rt) {
    this.visitElement(rt);
  }

  public <Z extends Element> void visitParentRt(Rt<Z> rt) {
    this.visitParent(rt);
  }

  public <Z extends Element> void visitElementRtc(Rtc<Z> rtc) {
    this.visitElement(rtc);
  }

  public <Z extends Element> void visitParentRtc(Rtc<Z> rtc) {
    this.visitParent(rtc);
  }

  public <Z extends Element> void visitElementRb(Rb<Z> rb) {
    this.visitElement(rb);
  }

  public <Z extends Element> void visitParentRb(Rb<Z> rb) {
    this.visitParent(rb);
  }

  public <Z extends Element> void visitElementRp(Rp<Z> rp) {
    this.visitElement(rp);
  }

  public <Z extends Element> void visitParentRp(Rp<Z> rp) {
    this.visitParent(rp);
  }

  public <Z extends Element> void visitElementBdo(Bdo<Z> bdo) {
    this.visitElement(bdo);
  }

  public <Z extends Element> void visitParentBdo(Bdo<Z> bdo) {
    this.visitParent(bdo);
  }

  public <Z extends Element> void visitElementSpan(Span<Z> span) {
    this.visitElement(span);
  }

  public <Z extends Element> void visitParentSpan(Span<Z> span) {
    this.visitParent(span);
  }

  public <Z extends Element> void visitElementIns(Ins<Z> ins) {
    this.visitElement(ins);
  }

  public <Z extends Element> void visitParentIns(Ins<Z> ins) {
    this.visitParent(ins);
  }

  public <Z extends Element> void visitElementDel(Del<Z> del) {
    this.visitElement(del);
  }

  public <Z extends Element> void visitParentDel(Del<Z> del) {
    this.visitParent(del);
  }

  public <Z extends Element> void visitElementFigcaption(Figcaption<Z> figcaption) {
    this.visitElement(figcaption);
  }

  public <Z extends Element> void visitParentFigcaption(Figcaption<Z> figcaption) {
    this.visitParent(figcaption);
  }

  public <Z extends Element> void visitElementFigure(Figure<Z> figure) {
    this.visitElement(figure);
  }

  public <Z extends Element> void visitParentFigure(Figure<Z> figure) {
    this.visitParent(figure);
  }

  public <Z extends Element> void visitElementImg(Img<Z> img) {
    this.visitElement(img);
  }

  public <Z extends Element> void visitParentImg(Img<Z> img) {
    this.visitParent(img);
  }

  public void visitAttributeAlt(String alt) {
    this.visitAttribute("alt", alt);
  }

  public void visitAttributeSrcset(String srcset) {
    this.visitAttribute("srcset", srcset);
  }

  public void visitAttributeUsemap(String usemap) {
    this.visitAttribute("usemap", usemap);
  }

  public void visitAttributeIsmap(String ismap) {
    this.visitAttributeBoolean("ismap", ismap);
  }

  public void visitAttributeWidth(String width) {
    this.visitAttribute("width", width);
  }

  public void visitAttributeHeight(String height) {
    this.visitAttribute("height", height);
  }

  public void visitAttributeLongdesc(String longdesc) {
    this.visitAttribute("longdesc", longdesc);
  }

  public <Z extends Element> void visitElementIframe(Iframe<Z> iframe) {
    this.visitElement(iframe);
  }

  public <Z extends Element> void visitParentIframe(Iframe<Z> iframe) {
    this.visitParent(iframe);
  }

  public void visitAttributeSrcDoc(String srcDoc) {
    this.visitAttribute("srcDoc", srcDoc);
  }

  public void visitAttributeSandbox(String sandbox) {
    this.visitAttribute("sandbox", sandbox);
  }

  public void visitAttributeAllowfullscreen(String allowfullscreen) {
    this.visitAttributeBoolean("allowfullscreen", allowfullscreen);
  }

  public void visitAttributeAllowpaymentrequest(String allowpaymentrequest) {
    this.visitAttributeBoolean("allowpaymentrequest", allowpaymentrequest);
  }

  public <Z extends Element> void visitElementEmbed(Embed<Z> embed) {
    this.visitElement(embed);
  }

  public <Z extends Element> void visitParentEmbed(Embed<Z> embed) {
    this.visitParent(embed);
  }

  public <Z extends Element> void visitElementObject(Object<Z> object) {
    this.visitElement(object);
  }

  public <Z extends Element> void visitParentObject(Object<Z> object) {
    this.visitParent(object);
  }

  public void visitAttributeData(String data) {
    this.visitAttribute("data", data);
  }

  public void visitAttributeTypemustmatch(String typemustmatch) {
    this.visitAttributeBoolean("typemustmatch", typemustmatch);
  }

  public void visitAttributeForm(String form) {
    this.visitAttribute("form", form);
  }

  public <Z extends Element> void visitElementParam(Param<Z> param) {
    this.visitElement(param);
  }

  public <Z extends Element> void visitParentParam(Param<Z> param) {
    this.visitParent(param);
  }

  public <Z extends Element> void visitElementDetails(Details<Z> details) {
    this.visitElement(details);
  }

  public <Z extends Element> void visitParentDetails(Details<Z> details) {
    this.visitParent(details);
  }

  public <Z extends Element> void visitParentDetailsComplete(DetailsComplete<Z> details) {
    this.visitParent(details);
  }

  public <Z extends Element> void visitElementLegend(Legend<Z> legend) {
    this.visitElement(legend);
  }

  public <Z extends Element> void visitParentLegend(Legend<Z> legend) {
    this.visitParent(legend);
  }

  public <Z extends Element> void visitElementDiv(Div<Z> div) {
    this.visitElement(div);
  }

  public <Z extends Element> void visitParentDiv(Div<Z> div) {
    this.visitParent(div);
  }

  public <Z extends Element> void visitElementSource(Source<Z> source) {
    this.visitElement(source);
  }

  public <Z extends Element> void visitParentSource(Source<Z> source) {
    this.visitParent(source);
  }

  public void visitAttributeSrcSet(String srcSet) {
    this.visitAttribute("srcSet", srcSet);
  }

  public <Z extends Element> void visitElementAudio(Audio<Z> audio) {
    this.visitElement(audio);
  }

  public <Z extends Element> void visitParentAudio(Audio<Z> audio) {
    this.visitParent(audio);
  }

  public void visitAttributePreload(String preload) {
    this.visitAttribute("preload", preload);
  }

  public void visitAttributeAutoplay(String autoplay) {
    this.visitAttribute("autoplay", autoplay);
  }

  public void visitAttributeLoop(String loop) {
    this.visitAttributeBoolean("loop", loop);
  }

  public void visitAttributeMuted(String muted) {
    this.visitAttributeBoolean("muted", muted);
  }

  public void visitAttributeControls(String controls) {
    this.visitAttribute("controls", controls);
  }

  public <Z extends Element> void visitElementTrack(Track<Z> track) {
    this.visitElement(track);
  }

  public <Z extends Element> void visitParentTrack(Track<Z> track) {
    this.visitParent(track);
  }

  public void visitAttributeKind(String kind) {
    this.visitAttribute("kind", kind);
  }

  public void visitAttributeSrclang(String srclang) {
    this.visitAttribute("srclang", srclang);
  }

  public void visitAttributeLabel(String label) {
    this.visitAttribute("label", label);
  }

  public void visitAttributeDefault(String var1) {
    this.visitAttributeBoolean("default", var1);
  }

  public <Z extends Element> void visitElementVideo(Video<Z> video) {
    this.visitElement(video);
  }

  public <Z extends Element> void visitParentVideo(Video<Z> video) {
    this.visitParent(video);
  }

  public void visitAttributePoster(String poster) {
    this.visitAttribute("poster", poster);
  }

  public <Z extends Element> void visitElementHr(Hr<Z> hr) {
    this.visitElement(hr);
  }

  public <Z extends Element> void visitParentHr(Hr<Z> hr) {
    this.visitParent(hr);
  }

  public <Z extends Element> void visitElementForm(Form<Z> form) {
    this.visitElement(form);
  }

  public <Z extends Element> void visitParentForm(Form<Z> form) {
    this.visitParent(form);
  }

  public void visitAttributeAcceptCharset(String acceptCharset) {
    this.visitAttribute("accept-charset", acceptCharset);
  }

  public void visitAttributeAction(String action) {
    this.visitAttribute("action", action);
  }

  public void visitAttributeAutocomplete(String autocomplete) {
    this.visitAttribute("autocomplete", autocomplete);
  }

  public void visitAttributeEnctype(String enctype) {
    this.visitAttribute("enctype", enctype);
  }

  public void visitAttributeMethod(String method) {
    this.visitAttribute("method", method);
  }

  public void visitAttributeNovalidate(String novalidate) {
    this.visitAttributeBoolean("novalidate", novalidate);
  }

  public <Z extends Element> void visitElementFieldset(Fieldset<Z> fieldset) {
    this.visitElement(fieldset);
  }

  public <Z extends Element> void visitParentFieldset(Fieldset<Z> fieldset) {
    this.visitParent(fieldset);
  }

  public void visitAttributeDisabled(String disabled) {
    this.visitAttributeBoolean("disabled", disabled);
  }

  public <Z extends Element> void visitElementLabel(Label<Z> label) {
    this.visitElement(label);
  }

  public <Z extends Element> void visitParentLabel(Label<Z> label) {
    this.visitParent(label);
  }

  public void visitAttributeFor(String var1) {
    this.visitAttribute("for", var1);
  }

  public <Z extends Element> void visitElementInput(Input<Z> input) {
    this.visitElement(input);
  }

  public <Z extends Element> void visitParentInput(Input<Z> input) {
    this.visitParent(input);
  }

  public void visitAttributeAccept(String accept) {
    this.visitAttribute("accept", accept);
  }

  public void visitAttributeChecked(String checked) {
    this.visitAttributeBoolean("checked", checked);
  }

  public void visitAttributeDirname(String dirname) {
    this.visitAttribute("dirname", dirname);
  }

  public void visitAttributeFormaction(String formaction) {
    this.visitAttribute("formaction", formaction);
  }

  public void visitAttributeFormenctype(String formenctype) {
    this.visitAttribute("formenctype", formenctype);
  }

  public void visitAttributeFormmethod(String formmethod) {
    this.visitAttribute("formmethod", formmethod);
  }

  public void visitAttributeFormnovalidate(String formnovalidate) {
    this.visitAttributeBoolean("formnovalidate", formnovalidate);
  }

  public void visitAttributeFormtarget(String formtarget) {
    this.visitAttribute("formtarget", formtarget);
  }

  public void visitAttributeList(String list) {
    this.visitAttribute("list", list);
  }

  public void visitAttributeMaxlength(String maxlength) {
    this.visitAttribute("maxlength", maxlength);
  }

  public void visitAttributeMinlength(String minlength) {
    this.visitAttribute("minlength", minlength);
  }

  public void visitAttributeMultiple(String multiple) {
    this.visitAttributeBoolean("multiple", multiple);
  }

  public void visitAttributePattern(String pattern) {
    this.visitAttribute("pattern", pattern);
  }

  public void visitAttributePlaceholder(String placeholder) {
    this.visitAttribute("placeholder", placeholder);
  }

  public void visitAttributeReadonly(String readonly) {
    this.visitAttributeBoolean("readonly", readonly);
  }

  public void visitAttributeRequired(String required) {
    this.visitAttributeBoolean("required", required);
  }

  public void visitAttributeSize(String size) {
    this.visitAttribute("size", size);
  }

  public void visitAttributeStep(String step) {
    this.visitAttribute("step", step);
  }

  public <Z extends Element> void visitElementButton(Button<Z> button) {
    this.visitElement(button);
  }

  public <Z extends Element> void visitParentButton(Button<Z> button) {
    this.visitParent(button);
  }

  public void visitAttributeAutofocus(String autofocus) {
    this.visitAttributeBoolean("autofocus", autofocus);
  }

  public <Z extends Element> void visitElementSelect(Select<Z> select) {
    this.visitElement(select);
  }

  public <Z extends Element> void visitParentSelect(Select<Z> select) {
    this.visitParent(select);
  }

  public void visitAttributeAutoComplete(String autoComplete) {
    this.visitAttribute("autoComplete", autoComplete);
  }

  public <Z extends Element> void visitElementDatalist(Datalist<Z> datalist) {
    this.visitElement(datalist);
  }

  public <Z extends Element> void visitParentDatalist(Datalist<Z> datalist) {
    this.visitParent(datalist);
  }

  public <Z extends Element> void visitElementOptgroup(Optgroup<Z> optgroup) {
    this.visitElement(optgroup);
  }

  public <Z extends Element> void visitParentOptgroup(Optgroup<Z> optgroup) {
    this.visitParent(optgroup);
  }

  public <Z extends Element> void visitElementOption(Option<Z> option) {
    this.visitElement(option);
  }

  public <Z extends Element> void visitParentOption(Option<Z> option) {
    this.visitParent(option);
  }

  public void visitAttributeSelected(String selected) {
    this.visitAttributeBoolean("selected", selected);
  }

  public <Z extends Element> void visitElementTextarea(Textarea<Z> textarea) {
    this.visitElement(textarea);
  }

  public <Z extends Element> void visitParentTextarea(Textarea<Z> textarea) {
    this.visitParent(textarea);
  }

  public void visitAttributeCols(String cols) {
    this.visitAttribute("cols", cols);
  }

  public void visitAttributeDirName(String dirName) {
    this.visitAttribute("dirName", dirName);
  }

  public void visitAttributeRows(String rows) {
    this.visitAttribute("rows", rows);
  }

  public void visitAttributeWrap(String wrap) {
    this.visitAttribute("wrap", wrap);
  }

  public <Z extends Element> void visitElementOutput(Output<Z> output) {
    this.visitElement(output);
  }

  public <Z extends Element> void visitParentOutput(Output<Z> output) {
    this.visitParent(output);
  }

  public <Z extends Element> void visitElementCanvas(Canvas<Z> canvas) {
    this.visitElement(canvas);
  }

  public <Z extends Element> void visitParentCanvas(Canvas<Z> canvas) {
    this.visitParent(canvas);
  }

  public <Z extends Element> void visitElementMap(Map<Z> map) {
    this.visitElement(map);
  }

  public <Z extends Element> void visitParentMap(Map<Z> map) {
    this.visitParent(map);
  }

  public <Z extends Element> void visitElementArea(Area<Z> area) {
    this.visitElement(area);
  }

  public <Z extends Element> void visitParentArea(Area<Z> area) {
    this.visitParent(area);
  }

  public void visitAttributeCoords(String coords) {
    this.visitAttribute("coords", coords);
  }

  public void visitAttributeShape(String shape) {
    this.visitAttribute("shape", shape);
  }

  public <Z extends Element> void visitElementMath(Math<Z> math) {
    this.visitElement(math);
  }

  public <Z extends Element> void visitParentMath(Math<Z> math) {
    this.visitParent(math);
  }

  public void visitAttributeDisplay(String display) {
    this.visitAttribute("display", display);
  }

  public void visitAttributeMaxwidth(String maxwidth) {
    this.visitAttribute("maxwidth", maxwidth);
  }

  public void visitAttributeOverflow(String overflow) {
    this.visitAttribute("overflow", overflow);
  }

  public void visitAttributeAltimg(String altimg) {
    this.visitAttribute("altimg", altimg);
  }

  public void visitAttributeAltimgWidth(String altimgWidth) {
    this.visitAttribute("altimg-width", altimgWidth);
  }

  public void visitAttributeAltimgHeigth(String altimgHeigth) {
    this.visitAttribute("altimg-heigth", altimgHeigth);
  }

  public void visitAttributeAltimgValign(String altimgValign) {
    this.visitAttribute("altimg-valign", altimgValign);
  }

  public void visitAttributeAlttext(String alttext) {
    this.visitAttribute("alttext", alttext);
  }

  public void visitAttributeCdgroup(String cdgroup) {
    this.visitAttribute("cdgroup", cdgroup);
  }

  public <Z extends Element> void visitElementSvg(Svg<Z> svg) {
    this.visitElement(svg);
  }

  public <Z extends Element> void visitParentSvg(Svg<Z> svg) {
    this.visitParent(svg);
  }

  public void visitAttributeX(String x) {
    this.visitAttribute("x", x);
  }

  public void visitAttributeY(String y) {
    this.visitAttribute("y", y);
  }

  public <Z extends Element> void visitElementTable(Table<Z> table) {
    this.visitElement(table);
  }

  public <Z extends Element> void visitParentTable(Table<Z> table) {
    this.visitParent(table);
  }

  public void visitAttributeBorder(String border) {
    this.visitAttribute("border", border);
  }

  public <Z extends Element> void visitElementCaption(Caption<Z> caption) {
    this.visitElement(caption);
  }

  public <Z extends Element> void visitParentCaption(Caption<Z> caption) {
    this.visitParent(caption);
  }

  public <Z extends Element> void visitElementColgroup(Colgroup<Z> colgroup) {
    this.visitElement(colgroup);
  }

  public <Z extends Element> void visitParentColgroup(Colgroup<Z> colgroup) {
    this.visitParent(colgroup);
  }

  public void visitAttributeSpan(String span) {
    this.visitAttribute("span", span);
  }

  public <Z extends Element> void visitElementCol(Col<Z> col) {
    this.visitElement(col);
  }

  public <Z extends Element> void visitParentCol(Col<Z> col) {
    this.visitParent(col);
  }

  public <Z extends Element> void visitElementTbody(Tbody<Z> tbody) {
    this.visitElement(tbody);
  }

  public <Z extends Element> void visitParentTbody(Tbody<Z> tbody) {
    this.visitParent(tbody);
  }

  public void visitAttributeRowgroup(String rowgroup) {
    this.visitAttribute("rowgroup", rowgroup);
  }

  public <Z extends Element> void visitElementThead(Thead<Z> thead) {
    this.visitElement(thead);
  }

  public <Z extends Element> void visitParentThead(Thead<Z> thead) {
    this.visitParent(thead);
  }

  public <Z extends Element> void visitElementTfoot(Tfoot<Z> tfoot) {
    this.visitElement(tfoot);
  }

  public <Z extends Element> void visitParentTfoot(Tfoot<Z> tfoot) {
    this.visitParent(tfoot);
  }

  public <Z extends Element> void visitElementTr(Tr<Z> tr) {
    this.visitElement(tr);
  }

  public <Z extends Element> void visitParentTr(Tr<Z> tr) {
    this.visitParent(tr);
  }

  public void visitAttributeRow(String row) {
    this.visitAttribute("row", row);
  }

  public <Z extends Element> void visitElementTd(Td<Z> td) {
    this.visitElement(td);
  }

  public <Z extends Element> void visitParentTd(Td<Z> td) {
    this.visitParent(td);
  }

  public void visitAttributeHeaders(String headers) {
    this.visitAttribute("headers", headers);
  }

  public void visitAttributeRowspan(String rowspan) {
    this.visitAttribute("rowspan", rowspan);
  }

  public void visitAttributeColspan(String colspan) {
    this.visitAttribute("colspan", colspan);
  }

  public void visitAttributeCell(String cell) {
    this.visitAttribute("cell", cell);
  }

  public <Z extends Element> void visitElementTh(Th<Z> th) {
    this.visitElement(th);
  }

  public <Z extends Element> void visitParentTh(Th<Z> th) {
    this.visitParent(th);
  }

  public void visitAttributeColumnheader(String columnheader) {
    this.visitAttribute("columnheader", columnheader);
  }

  public void visitAttributeRowheader(String rowheader) {
    this.visitAttribute("rowheader", rowheader);
  }

  public void visitAttributeAbbr(String abbr) {
    this.visitAttribute("abbr", abbr);
  }

  public void visitAttributeScope(String scope) {
    this.visitAttribute("scope", scope);
  }

  public <Z extends Element> void visitElementMain(Main<Z> main) {
    this.visitElement(main);
  }

  public <Z extends Element> void visitParentMain(Main<Z> main) {
    this.visitParent(main);
  }

  public <Z extends Element> void visitElementTemplate(Template<Z> template) {
    this.visitElement(template);
  }

  public <Z extends Element> void visitParentTemplate(Template<Z> template) {
    this.visitParent(template);
  }

  public <Z extends Element> void visitElementBdi(Bdi<Z> bdi) {
    this.visitElement(bdi);
  }

  public <Z extends Element> void visitParentBdi(Bdi<Z> bdi) {
    this.visitParent(bdi);
  }

  public <Z extends Element> void visitElementData(Data<Z> data) {
    this.visitElement(data);
  }

  public <Z extends Element> void visitParentData(Data<Z> data) {
    this.visitParent(data);
  }

  public <Z extends Element> void visitElementS(S<Z> s) {
    this.visitElement(s);
  }

  public <Z extends Element> void visitParentS(S<Z> s) {
    this.visitParent(s);
  }

  public <Z extends Element> void visitElementU(U<Z> u) {
    this.visitElement(u);
  }

  public <Z extends Element> void visitParentU(U<Z> u) {
    this.visitParent(u);
  }

  public <Z extends Element> void visitElementWbr(Wbr<Z> wbr) {
    this.visitElement(wbr);
  }

  public <Z extends Element> void visitParentWbr(Wbr<Z> wbr) {
    this.visitParent(wbr);
  }

  public <Z extends Element> void visitElementPicture(Picture<Z> picture) {
    this.visitElement(picture);
  }

  public <Z extends Element> void visitParentPicture(Picture<Z> picture) {
    this.visitParent(picture);
  }

  public <Z extends Element> void visitElementSummary(Summary<Z> summary) {
    this.visitElement(summary);
  }

  public <Z extends Element> void visitParentSummary(Summary<Z> summary) {
    this.visitParent(summary);
  }

  public <Z extends Element> void visitElementRoot(Root<Z> root) {
    this.visitElement(root);
  }

  public <Z extends Element> void visitParentRoot(Root<Z> root) {
    this.visitParent(root);
  }

  public void visitAttributeOnautocomplete(String onautocomplete) {
    this.visitAttribute("onautocomplete", onautocomplete);
  }

  public void visitAttributeOnautocompleteerror(String onautocompleteerror) {
    this.visitAttribute("onautocompleteerror", onautocompleteerror);
  }

  public void visitAttributeOncontextmenu(String oncontextmenu) {
    this.visitAttribute("oncontextmenu", oncontextmenu);
  }

  public void visitAttributeOnsort(String onsort) {
    this.visitAttribute("onsort", onsort);
  }

  public void visitAttributeOnabort(String onabort) {
    this.visitAttribute("onabort", onabort);
  }

  public void visitAttributeOnauxclick(String onauxclick) {
    this.visitAttribute("onauxclick", onauxclick);
  }

  public void visitAttributeOnblur(String onblur) {
    this.visitAttribute("onblur", onblur);
  }

  public void visitAttributeOncancel(String oncancel) {
    this.visitAttribute("oncancel", oncancel);
  }

  public void visitAttributeOncanplay(String oncanplay) {
    this.visitAttribute("oncanplay", oncanplay);
  }

  public void visitAttributeOncanplaythrough(String oncanplaythrough) {
    this.visitAttribute("oncanplaythrough", oncanplaythrough);
  }

  public void visitAttributeOnchange(String onchange) {
    this.visitAttribute("onchange", onchange);
  }

  public void visitAttributeOnclick(String onclick) {
    this.visitAttribute("onclick", onclick);
  }

  public void visitAttributeOnclose(String onclose) {
    this.visitAttribute("onclose", onclose);
  }

  public void visitAttributeOncuechange(String oncuechange) {
    this.visitAttribute("oncuechange", oncuechange);
  }

  public void visitAttributeOndblclick(String ondblclick) {
    this.visitAttribute("ondblclick", ondblclick);
  }

  public void visitAttributeOndrag(String ondrag) {
    this.visitAttribute("ondrag", ondrag);
  }

  public void visitAttributeOndragend(String ondragend) {
    this.visitAttribute("ondragend", ondragend);
  }

  public void visitAttributeOndragenter(String ondragenter) {
    this.visitAttribute("ondragenter", ondragenter);
  }

  public void visitAttributeOndragexit(String ondragexit) {
    this.visitAttribute("ondragexit", ondragexit);
  }

  public void visitAttributeOndragleave(String ondragleave) {
    this.visitAttribute("ondragleave", ondragleave);
  }

  public void visitAttributeOndragover(String ondragover) {
    this.visitAttribute("ondragover", ondragover);
  }

  public void visitAttributeOndragstart(String ondragstart) {
    this.visitAttribute("ondragstart", ondragstart);
  }

  public void visitAttributeOndrop(String ondrop) {
    this.visitAttribute("ondrop", ondrop);
  }

  public void visitAttributeOndurationchange(String ondurationchange) {
    this.visitAttribute("ondurationchange", ondurationchange);
  }

  public void visitAttributeOnemptied(String onemptied) {
    this.visitAttribute("onemptied", onemptied);
  }

  public void visitAttributeOnended(String onended) {
    this.visitAttribute("onended", onended);
  }

  public void visitAttributeOnerror(String onerror) {
    this.visitAttribute("onerror", onerror);
  }

  public void visitAttributeOnfocus(String onfocus) {
    this.visitAttribute("onfocus", onfocus);
  }

  public void visitAttributeOninput(String oninput) {
    this.visitAttribute("oninput", oninput);
  }

  public void visitAttributeOninvalid(String oninvalid) {
    this.visitAttribute("oninvalid", oninvalid);
  }

  public void visitAttributeOnkeydown(String onkeydown) {
    this.visitAttribute("onkeydown", onkeydown);
  }

  public void visitAttributeOnkeypress(String onkeypress) {
    this.visitAttribute("onkeypress", onkeypress);
  }

  public void visitAttributeOnkeyup(String onkeyup) {
    this.visitAttribute("onkeyup", onkeyup);
  }

  public void visitAttributeOnload(String onload) {
    this.visitAttribute("onload", onload);
  }

  public void visitAttributeOnloadeddata(String onloadeddata) {
    this.visitAttribute("onloadeddata", onloadeddata);
  }

  public void visitAttributeOnloadedmetadata(String onloadedmetadata) {
    this.visitAttribute("onloadedmetadata", onloadedmetadata);
  }

  public void visitAttributeOnloadend(String onloadend) {
    this.visitAttribute("onloadend", onloadend);
  }

  public void visitAttributeOnloadstart(String onloadstart) {
    this.visitAttribute("onloadstart", onloadstart);
  }

  public void visitAttributeOnmousedown(String onmousedown) {
    this.visitAttribute("onmousedown", onmousedown);
  }

  public void visitAttributeOnmouseenter(String onmouseenter) {
    this.visitAttribute("onmouseenter", onmouseenter);
  }

  public void visitAttributeOnmouseleave(String onmouseleave) {
    this.visitAttribute("onmouseleave", onmouseleave);
  }

  public void visitAttributeOnmousemove(String onmousemove) {
    this.visitAttribute("onmousemove", onmousemove);
  }

  public void visitAttributeOnmouseout(String onmouseout) {
    this.visitAttribute("onmouseout", onmouseout);
  }

  public void visitAttributeOnmouseover(String onmouseover) {
    this.visitAttribute("onmouseover", onmouseover);
  }

  public void visitAttributeOnmouseup(String onmouseup) {
    this.visitAttribute("onmouseup", onmouseup);
  }

  public void visitAttributeOnwheel(String onwheel) {
    this.visitAttribute("onwheel", onwheel);
  }

  public void visitAttributeOnpause(String onpause) {
    this.visitAttribute("onpause", onpause);
  }

  public void visitAttributeOnplay(String onplay) {
    this.visitAttribute("onplay", onplay);
  }

  public void visitAttributeOnplaying(String onplaying) {
    this.visitAttribute("onplaying", onplaying);
  }

  public void visitAttributeOnprogress(String onprogress) {
    this.visitAttribute("onprogress", onprogress);
  }

  public void visitAttributeOnratechange(String onratechange) {
    this.visitAttribute("onratechange", onratechange);
  }

  public void visitAttributeOnreset(String onreset) {
    this.visitAttribute("onreset", onreset);
  }

  public void visitAttributeOnresize(String onresize) {
    this.visitAttribute("onresize", onresize);
  }

  public void visitAttributeOnscroll(String onscroll) {
    this.visitAttribute("onscroll", onscroll);
  }

  public void visitAttributeOnseeked(String onseeked) {
    this.visitAttribute("onseeked", onseeked);
  }

  public void visitAttributeOnseeking(String onseeking) {
    this.visitAttribute("onseeking", onseeking);
  }

  public void visitAttributeOnselect(String onselect) {
    this.visitAttribute("onselect", onselect);
  }

  public void visitAttributeOnshow(String onshow) {
    this.visitAttribute("onshow", onshow);
  }

  public void visitAttributeOnstalled(String onstalled) {
    this.visitAttribute("onstalled", onstalled);
  }

  public void visitAttributeOnsubmit(String onsubmit) {
    this.visitAttribute("onsubmit", onsubmit);
  }

  public void visitAttributeOnsuspend(String onsuspend) {
    this.visitAttribute("onsuspend", onsuspend);
  }

  public void visitAttributeOntimeupdate(String ontimeupdate) {
    this.visitAttribute("ontimeupdate", ontimeupdate);
  }

  public void visitAttributeOntoogle(String ontoogle) {
    this.visitAttribute("ontoogle", ontoogle);
  }

  public void visitAttributeOnvolumenchange(String onvolumenchange) {
    this.visitAttribute("onvolumenchange", onvolumenchange);
  }

  public void visitAttributeOnwaiting(String onwaiting) {
    this.visitAttribute("onwaiting", onwaiting);
  }

  public void visitAttributeAccesskey(String accesskey) {
    this.visitAttribute("accesskey", accesskey);
  }

  public void visitAttributeClass(String var1) {
    this.visitAttribute("class", var1);
  }

  public void visitAttributeContenteditable(String contenteditable) {
    this.visitAttribute("contenteditable", contenteditable);
  }

  public void visitAttributeDir(String dir) {
    this.visitAttribute("dir", dir);
  }

  public void visitAttributeDraggable(String draggable) {
    this.visitAttribute("draggable", draggable);
  }

  public void visitAttributeHidden(String hidden) {
    this.visitAttributeBoolean("hidden", hidden);
  }

  public void visitAttributeId(String id) {
    this.visitAttribute("id", id);
  }

  public void visitAttributeLang(String lang) {
    this.visitAttribute("lang", lang);
  }

  public void visitAttributeSpellcheck(String spellcheck) {
    this.visitAttribute("spellcheck", spellcheck);
  }

  public void visitAttributeStyle(String style) {
    this.visitAttribute("style", style);
  }

  public void visitAttributeTabIndex(String tabIndex) {
    this.visitAttribute("tabIndex", tabIndex);
  }

  public void visitAttributeTitle(String title) {
    this.visitAttribute("title", title);
  }

  public void visitAttributeTranslate(String translate) {
    this.visitAttribute("translate", translate);
  }

  public <Z extends Element> void visitParentDetailsSummary(DetailsSummary<Z> Details) {
    this.visitParent(Details);
  }
}
