package com.adobe.aem.devbot.langchain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PRService {
    private final RestTemplate restTemplate;

    public PRService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getPRContent(String prLink) {
        return restTemplate.getForObject(prLink + ".diff", String.class);
    }
}
