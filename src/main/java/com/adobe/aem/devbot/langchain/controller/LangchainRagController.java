package com.adobe.aem.devbot.langchain.controller;

import com.adobe.aem.devbot.langchain.service.LangchainRagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/devbot")
public class LangchainRagController {
    private final LangchainRagService langchainRagService;
    public LangchainRagController(LangchainRagService langchainRagService) {
        this.langchainRagService = langchainRagService;
    }
    @GetMapping("/generate/rag")
    public String generate(@RequestParam(value = "message",
            defaultValue = "Please analise the code changes in this PR and return a list of found issues") String message) throws IOException {
        return langchainRagService.retrieve(message);
    }
}
