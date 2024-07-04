package com.adobe.aem.devbot.langchain.controller;

import com.adobe.aem.devbot.langchain.service.LangchainMasterBotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/devbot")
public class LangchainMasterBotController {

    private final LangchainMasterBotService langchainMasterBotService;

    public LangchainMasterBotController(LangchainMasterBotService langchainMasterBotService) {
        this.langchainMasterBotService = langchainMasterBotService;
    }

    @GetMapping("/generate/master")
    public String generateResponseForMaster(@RequestParam(value = "message", defaultValue = "Tell me about Oak and Sling") String message) throws IOException {
        return langchainMasterBotService.retrieve(message);
    }

    //TODO: endpoint for passing a PR link and responding based on its content

}
