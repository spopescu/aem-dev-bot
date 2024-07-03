package com.adobe.aem.devbot.langchain.configuration;

import com.adobe.aem.devbot.langchain.service.SpecializedBotService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LangchainDelegationConfig {

    private final SpecializedBotService specializedBotService;

    public LangchainDelegationConfig(SpecializedBotService specializedBotService)  {
        this.specializedBotService = specializedBotService;
    }

    @Tool("Answers a question about JCR code.")
    public String getAnswerForJcrCode(String question) {
        return specializedBotService.getAnswerForJcrCode(question);
    }

    @Tool("Answers a question about Sling code.")
    public String getAnswerForSlingCode(String question) {
        return specializedBotService.getAnswerForSlingCode(question);
    }
}
