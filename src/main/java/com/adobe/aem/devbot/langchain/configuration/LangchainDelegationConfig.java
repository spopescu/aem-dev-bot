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

    @Tool("""
    Asks the Oak bot questions about code and returns the answer.
    You will ask questions about the code to the Oak bot and get the answer for each question. Questions will be similar to the following:
        - What is the impact of this code on the Oak repository?
        - Does this code follow best practices for Oak?
        - Does this code have any potential issues?
        - Does this code have any security vulnerabilities?
        - Does this code have any potential NPEs?
        - Does this code have any potential performance issues?
        - Does this code have any potential memory leaks?
        - Does this code have any potential thread safety issues?
    """)
    public String getAnswerForOakCode(@P("""
        A set of questions about a PR's code, additionally providing the code itself.
            """)
                                          String questionsAndPrCode) {
        return specializedBotService.getAnswerForOakCode(questionsAndPrCode);
    }

    @Tool("""
    Asks the Sling bot questions about code and returns the answer.
    You will ask questions about the code to the Sling bot and get the answer for each question. Questions will be similar to the following:
        - What is the impact of this code on Sling components?
        - Does this code follow best practices for Sling?
        - Does this code have any potential issues?
        - Does this code have any security vulnerabilities?
        - Does this code have any potential NPEs?
        - Does this code have any potential performance issues?
        - Does this code have any potential memory leaks?
        - Does this code have any potential thread safety issues?
    """)
    public String getAnswerForSlingCode(@P("""
        A set of questions about a PR's code, additionally providing the code itself.
            """)
                                            String questionsAndPrCode) {
        return specializedBotService.getAnswerForSlingCode(questionsAndPrCode);
    }
}
