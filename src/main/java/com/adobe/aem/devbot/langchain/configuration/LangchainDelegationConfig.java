package com.adobe.aem.devbot.langchain.configuration;

import com.adobe.aem.devbot.langchain.service.SpecializedBotService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class LangchainDelegationConfig {

    private final SpecializedBotService specializedBotService;

    public LangchainDelegationConfig(SpecializedBotService specializedBotService)  {
        this.specializedBotService = specializedBotService;
    }

    @Tool("Answers a question about Oak code.")
    public String getAnswerForOakCode(String question) {
        return specializedBotService.getAnswerForOakCode(question);
    }

    @Tool("Answers a question about Sling code.")
    public String getAnswerForSlingCode(String question) {
        return specializedBotService.getAnswerForSlingCode(question);
    }
}
