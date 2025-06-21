package org.xmlet.htmlapifaster;


public interface MfeConfiguration {
    String getMfeUrlResource();

    String getMfeElementName();

    String getMfeName();

    String getMfeListeningEventName();

    String getMfeTriggerEventName();

    String getMfeScriptUrl();

    String getMfeStylingUrl();

    boolean isMfeStreamingData();

    boolean setMfeStreamingData(boolean streamingData);

    String setMfeUrlResource(String mfeUrlResource);

    String setMfeName(String mfeName);

    String setMfeListeningEventName(String mfeListeningEventName);

    String setMfeTriggersEventName(String mfeTriggersEventName);

    String setMfeElementName(String mfeElementName);

    String setMfeScriptUrl(String mfeScriptUrl);

    String setMfeStylingUrl(String mfeStylingUrl);
}
