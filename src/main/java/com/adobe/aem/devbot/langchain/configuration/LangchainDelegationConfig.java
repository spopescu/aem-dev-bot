package com.adobe.aem.devbot.langchain.configuration;

import dev.langchain4j.agent.tool.Tool;

import java.util.Map;

public class LangchainDelegationConfig {

    @Tool("Answers a question about JCR code.")
    public String getAnswerForJcrCode(String question) {
        return "result of POST request to JCR bot";
    }

    @Tool("Answers a question about Sling code.")
    public String getAnswerForSlingCode(String question) {
        return "result of POST request to Sling bot";
    }
}
