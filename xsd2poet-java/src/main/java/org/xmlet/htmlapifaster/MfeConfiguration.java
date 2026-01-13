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

    MfeConfiguration setMfeStreamingData(boolean streamingData);

    MfeConfiguration setMfeUrlResource(String mfeUrlResource);

    MfeConfiguration setMfeName(String mfeName);

    MfeConfiguration setMfeListeningEventName(String mfeListeningEventName);

    MfeConfiguration setMfeTriggersEventName(String mfeTriggersEventName);

    MfeConfiguration setMfeElementName(String mfeElementName);

    MfeConfiguration setMfeScriptUrl(String mfeScriptUrl);

    MfeConfiguration setMfeStylingUrl(String mfeStylingUrl);
}
