package org.xmlet.htmlapifaster;


public interface MfeConfiguration {
    String getMfeUrlResource();
    String getMfeElementName();
    String getMfeName();
    String getMfeListeningEventName();
    String getMfeTriggerEventName();
    String getMfeScriptUrl();
    String getMfeStylingUrl();
    String getMfeSharedStylingUrl();
    String getMfeScriptIntegrity();
    boolean isMfeStreamingData();
}
