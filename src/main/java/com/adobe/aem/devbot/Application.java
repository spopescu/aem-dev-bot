package com.adobe.aem.devbot;

import com.adobe.aem.devbot.langchain.configuration.AzureOpenAICredentials;
import com.adobe.aem.devbot.langchain.configuration.SpecializedBotsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AzureOpenAICredentials.class, SpecializedBotsConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
