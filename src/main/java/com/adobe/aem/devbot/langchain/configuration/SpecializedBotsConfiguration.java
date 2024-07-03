package com.adobe.aem.devbot.langchain.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.bot")
public class SpecializedBotsConfiguration {
    private String jcrBotUrl;
    private String slingBotUrl;

    public String getJcrBotUrl() {
        return jcrBotUrl;
    }

    public void setJcrBotUrl(String jcrBotUrl) {
        this.jcrBotUrl = jcrBotUrl;
    }

    public String getSlingBotUrl() {
        return slingBotUrl;
    }

    public void setSlingBotUrl(String slingBotUrl) {
        this.slingBotUrl = slingBotUrl;
    }
}
