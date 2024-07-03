package com.adobe.aem.devbot.langchain.controller;

import com.adobe.aem.devbot.langchain.service.LangchainDelegationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devbot")
public class LangchainFunctionController {
    private final LangchainDelegationService langchainDelegationService;
    public LangchainFunctionController(LangchainDelegationService langchainDelegationService) {
        this.langchainDelegationService = langchainDelegationService;
    }
    @GetMapping("/generate/function")
    public String generateFunctionResultForMessage(@RequestParam(value = "message", defaultValue = "Please analise the code changes in this PR and return a list of found issues. Delegate questions specific to JCR or Sling code.") String message) {
        return langchainDelegationService.call(message);
    }
}
