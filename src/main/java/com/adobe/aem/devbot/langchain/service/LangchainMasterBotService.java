package com.adobe.aem.devbot.langchain.service;

import com.adobe.aem.devbot.langchain.configuration.LangchainDelegationConfig;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LangchainMasterBotService {

    @Value("classpath:/data/oak-next-gen-repository.txt")
    private Resource markerResource;

    private final ChatLanguageModel chatLanguageModel;

    private final LangchainDelegationService langchainDelegationService;

    private final LangchainDelegationConfig localizedBotsConfig;

    // TODO: Delimit source PR content and modified PR content; tell the model which is which, and include both
    private final String PR_REVIEW_PREFIX = """
            You will now receive PR content between ~~~ characters. You will ONLY review the code between ~~~, not any other code, regardless of what other code you see.
            Your review will be done according to factors such as best practices, coding standards, and possible issues like NPEs or security vulnerabilities.
            You will assemble the review into a JSON response in the following format: {"approved": true/false, "explanation": (reasoning for approval)}.
            You will respond with ONLY this json, without any formatting around it.
            ~~~
            """;

    private final String PR_REVIEW_POSTFIX = """
            ~~~
            """;

    public LangchainMasterBotService(ChatLanguageModel chatLanguageModel, LangchainDelegationService langchainDelegationService, LangchainDelegationConfig langchainDelegationConfig) {
        this.chatLanguageModel = chatLanguageModel;
        this.langchainDelegationService = langchainDelegationService;
        this.localizedBotsConfig = langchainDelegationConfig;
    }

    public String retrieveNormalMessage(String message) throws IOException {
        // Create an instance of your Assistant AiService

        // Load documents
        String dataFolderPath = markerResource.getFile().getParent();
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(dataFolderPath);

        // Initialize vector store and ingest the documents into it
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        // Move your AI Service down here and make it point to the vector store
        LangchainMasterBotService.Assistant assistant = AiServices.builder(LangchainMasterBotService.Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                // uncomment to allow calling external bots
//                .tools(localizedBotsConfig)
                .build();
        return assistant.chat(message);
    }

    public String reviewPrContent(String prContent) throws IOException {
        String modelInput = PR_REVIEW_PREFIX + prContent + PR_REVIEW_POSTFIX;
        // Create an instance of your Assistant AiService

        // Load documents
        String dataFolderPath = markerResource.getFile().getParent();
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(dataFolderPath);

        // Initialize vector store and ingest the documents into it
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        // Move your AI Service down here and make it point to the vector store
        LangchainMasterBotService.Assistant assistant = AiServices.builder(LangchainMasterBotService.Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                // uncomment to allow calling external bots
//                .tools(localizedBotsConfig)
                .build();
        return assistant.chat(modelInput);
    }

    // Create an Assistant interface with a chat method
    interface Assistant {
        @SystemMessage("""
            You are AEM Dev Bot, an expert programmer that analyses content in Github Pull Requests.
            You will apply best practices and coding standards to the code changes and provide a list of found issues, if any.
            You will also provide a detailed explanation of the issues and suggest possible solutions.
            You are assisted by a collection of specialized bots, each specialized on a different Git repository of AEM code.
            You will delegate questions specific to Oak or Sling code to the appropriate bot.
            """)
        String chat(String userMessage);
    }
}
