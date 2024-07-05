package com.adobe.aem.devbot.langchain.configuration;

import com.adobe.aem.devbot.langchain.service.SpecializedBotService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class LangchainDelegationConfig {

    private final SpecializedBotService specializedBotService;

    public LangchainDelegationConfig(SpecializedBotService specializedBotService)  {
        this.specializedBotService = specializedBotService;
    }

    @Tool("Asks the Oak bot a question about code and returns the answer.")
    public String getAnswerForOakCode(@P("A question to ask the Oak bot about the given code, such as questions about whether this code impacts other Oak components")
                                          String question) {
        return specializedBotService.getAnswerForOakCode(question);
    }

    @Tool("Asks the Sling bot a question about code and returns the answer.")
    public String getAnswerForSlingCode(@P("A question to ask the Sling bot about the given code, such as questions about whether this code impacts other Sling components")
                                            String question) {
        return specializedBotService.getAnswerForSlingCode(question);
    }
}
