package org.xmlet.htmlapifaster;

public interface MfeConfigurationBuilder extends MfeConfiguration {
    MfeConfigurationBuilder setMfeStreamingData(boolean streamingData);
    MfeConfigurationBuilder setMfeUrlResource(String mfeUrlResource);
    MfeConfigurationBuilder setMfeName(String mfeName);
    MfeConfigurationBuilder setMfeListeningEventName(String mfeListeningEventName);
    MfeConfigurationBuilder setMfeTriggersEventName(String mfeTriggersEventName);
    MfeConfigurationBuilder setMfeElementName(String mfeElementName);
    MfeConfigurationBuilder setMfeScriptUrl(String mfeScriptUrl);
    MfeConfigurationBuilder setMfeStylingUrl(String mfeStylingUrl);
    MfeConfigurationBuilder setMfeScriptIntegrity(String mfeScriptIntegrity);
}
