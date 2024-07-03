package com.adobe.aem.devbot.langchain.controller;

import com.adobe.aem.devbot.langchain.service.LangchainPromptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/devbot")
public class LangchainPromptController {
    private final LangchainPromptService langchainPromptService;

    public LangchainPromptController(LangchainPromptService langchainPromptService) {
        this.langchainPromptService = langchainPromptService;
    }

    @GetMapping("/generate/prompt")
    public String generateResponseForPrompt(@RequestParam(value = "message", defaultValue = "Tell me about three famous physicists from the last century. Write at least a sentence for each physicist.") String message,
                                            @RequestParam(value = "name", defaultValue = "Bob") String name,
                                            @RequestParam(value = "voice", defaultValue = "pirate") String voice) throws IOException {

        // Use the call method to generate a response for the given prompt
        return langchainPromptService.call(message, name, voice);
    }
}
