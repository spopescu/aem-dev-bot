package com.adobe.aem.devbot.langchain.service;

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
public class LangchainRagService {
    @Value("classpath:/data/oak-best-practices.txt")
    private Resource testResource;

    private final ChatLanguageModel chatLanguageModel;

    public LangchainRagService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String retrieve(String message) throws IOException {
        // Create an instance of your Assistant AiService

        // Load documents
        String path = testResource.getFile().getParent();
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(path);

        // Initialize vector store and ingest the documents into it
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        // Move your AI Service down here and make it point to the vector store
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
        return assistant.chat(message);
    }

    // Create an Assistant interface with a chat method
    interface Assistant {
        @SystemMessage("""
            You are AEM Dev Bot, an expert programmer that analyses code changes in Git Pull Requests.
            You will apply best practices and coding standards to the code changes and provide a list of found issues.
            You will also provide a detailed explanation of the issues and suggest possible solutions.
            You are assisted by a collection of specialized bots, each specialized on a different Git repository of AEM code.
            You will delegate questions specific to JCR or Sling code to the appropriate bot.
            You will then collect the answers and assemble them into a JSON response. 
            """)
        String chat(String userMessage);
    }
}
