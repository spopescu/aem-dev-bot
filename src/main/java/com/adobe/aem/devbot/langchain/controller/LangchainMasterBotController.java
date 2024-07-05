package com.adobe.aem.devbot.langchain.controller;

import com.adobe.aem.devbot.langchain.service.LangchainMasterBotService;
import com.adobe.aem.devbot.langchain.service.PRService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/devbot")
public class LangchainMasterBotController {

    private final LangchainMasterBotService langchainMasterBotService;

    private final PRService prService;

    public LangchainMasterBotController(LangchainMasterBotService langchainMasterBotService, PRService prService) {
        this.langchainMasterBotService = langchainMasterBotService;
        this.prService = prService;
    }

    @GetMapping("/generate/master/chat")
    public String generateResponseForMaster(@RequestParam(value = "message", defaultValue = "Tell me about Oak and Sling") String message) throws IOException {
        String botResponse = langchainMasterBotService.retrieveNormalMessage(message);
        return botResponse;
    }

    @GetMapping("/generate/master/pr_content")
    public String generateResponseForMasterPrContent(@RequestParam(value = "message", defaultValue = "int a = 2;") String message) throws IOException {
        String botResponse = langchainMasterBotService.reviewPrContent(message);
        return botResponse;
    }

    //endpoint for passing a PR link and responding based on its content
    @GetMapping("/generate/master/pr_link")
    public String generateResponseForMasterPrLink(@RequestParam(value = "prLink") String prLink) throws IOException {
        String prContent =  prService.getPRContent(prLink);
        String botResponse = langchainMasterBotService.reviewPrContent(prContent);
        return botResponse;
    }
}
