package com.adobe.aem.devbot.langchain.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.bot")
public class SpecializedBotsConfiguration {
    private String oakBotUrl;
    private String slingBotUrl;

    public String getOakBotUrl() {
        return oakBotUrl;
    }

    public void setOakBotUrl(String oakBotUrl) {
        this.oakBotUrl = oakBotUrl;
    }

    public String getSlingBotUrl() {
        return slingBotUrl;
    }

    public void setSlingBotUrl(String slingBotUrl) {
        this.slingBotUrl = slingBotUrl;
    }
}
