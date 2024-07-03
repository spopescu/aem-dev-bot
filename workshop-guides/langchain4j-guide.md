# Langchain4j Guide

This step by step guide will help you to go through the Langchain4j code in this repo and explore the capabilities of this
framework in the Gen AI space.

## 0. What is Langchain4j?

Langchain4j is a Java framework whose goal is to simplify integrating AI/LLM capabilities into Java applications. Its development began in
early 2023 as a response to the lack of Java counterparts to the numerous Python and JavaScript LLM libraries and frameworks.
Although **Langchain** is in the name, it is not the same project as the LangChain library in Python. It draws inspiration from LangChain, Haystack, 
Llamaindex and the broader community.

### Links

* Langchain4j documentation: https://docs.langchain4j.dev/intro

## 1. Prerequisites

The first step is to download this code on your local workstation (if you haven't done it already) and import it into Intellij Idea:
```shell
git clone git@git.corp.adobe.com:gen-ai-enablement/java-workshop.git
```

Make sure to switch to the `langchain4j` branch
```shell
git checkout langchain4j
```

## 2. Set up the Azure Open AI API KEY

We will provide you the API Key via secretshare once the workshop starts.

Once you have the API key, you need to add it to the `application.properties` file in the `src/main/resources` folder.

## 3. Experiment simple interactions with Azure Open AI

In this step, you will learn how to interact with Azure Open AI using two components provided by Langchain4j. We will start with `ChatLanguageModel`, which is a low-level API that
offers the most power and flexibility. Lastly, we will learn about AIServices, which is the high-level counterpart.

The code we explore in this step is in 2 files:
* Beans.java (contains the definition of the `ChatLanguageModel` bean)
* LangchainChatController.java

### 3.1 Use ChatLanguageModel object to interact with Azure Open AI

Update `simpleStringMessage` in `LangchainChatController.java` file to interact with Azure Open AI using the `chatLanguageModel` object. This returned value is simply a String representing the LLM's output.

Run the app and make a simple request, by running this command (or using any HTTP client/your own browser/etc):
```shell
curl localhost:8080/langchain/generate/1
```

Explore by passing any message as a parameter to the above curl command:
```shell
curl "localhost:8080/langchain/generate/1?message=What%20is%20the%20capital%20of%20France%3F"
```
Note: the message is URL encoded. To do that yourself, use this online tool to convert a string into a URL encoded string: https://www.urlencoder.org/



<details>
  <summary>Solution here</summary>

    return chatLanguageModel.generate(message);

</details>


### 3.2 Interact using a more powerful API

The API at 3.1 is the simplest, having a String as both input and output. There is a more powerful version of that API, allowing you to:
- pass in images as input 
- get back more info than just the LLM's output
- send multiple messages
and more.

Update the `simplePromptMessage` method in the `LangchainChatController.java` file to wrap your message in a UserMessage, then pass it to the LLM.

You can test this by running the following command:
```shell
curl localhost:8080/langchain/generate/2
```
<details>
  <summary>Solution here</summary>

    Response<AiMessage> response = chatLanguageModel.generate(UserMessage.from(message));
    FinishReason finishReason = response.finishReason();
    logger.info("Finish reason: {}", finishReason);
    TokenUsage tokenUsage = response.tokenUsage();
    logger.info("Token usage: {}", tokenUsage);
    return response.content().text();

</details>


### 3.3 Pass multiple messages to the LLM

LLMs are stateless, meaning they don't remember the previous messages you sent to them. The context of the conversation will need
to be managed at an application-level. A simple approach is to hold the conversation history in a list and then pass the messages together.

Update the `simplePromptMultipleMessages` method in the `LangchainChatController.java` to pass multiple messages to the LLM.

You can test this by running the following command:
```shell
curl localhost:8080/generate/3
```

<details>
  <summary>Solution here</summary>

    List<ChatMessage> chatMessages = Arrays.stream(message).map(UserMessage::from).collect(Collectors.toList());
    return chatLanguageModel.generate(chatMessages).content().text();

</details>

### 3.4 Use an AI Service to interact with Azure Open AI

In Python's LangChain we have the concept of chains, which basically is a way to facilitate interaction between multiple low-level
components. Langchain4j has only two chains implemented and there are no plans of adding more, being considered too rigid. 
AI Services is proposed as the way to go in Java.

The idea of an AI Service is to hide the complexities of interacting with LLMs and other components behind a simple API. To start, 
we must define an interface for our AIService and then provide it along with other low-level components to Langchain4j, which is going
to created a proxy object implementing the interface.

In `LangchainChatController.java` you have already such an interface defined. Use the `AIServices` builder to create an instance of it. You will 
need to pass your interface class to the `builder` method and then use `chatLanguageModel` to provide the component we used earlier. 
Make sure to update the `simplePromptForAIService` method in the `LangchainChatController.java`.

You can test this by running the following command:
```shell
curl localhost:8080/langchain/generate/4
```

<details>
  <summary>Solution here</summary>

    private final Assistant assistant;

    // in constructor
    this.assistant = AiServices.builder(Assistant.class).chatLanguageModel(chatLanguageModel).build();

    // in simplePromptForAIService method
    return assistant.chat(message);

</details>

### 3.5 Augment the AI Service with memory

Langchain4j comes with mechanisms to manage memory out of the box. If you go back to the AiServices builder from 3.4 and use autocomplete,
you will notice it has a `chatMemory` method.  

Initialize a `ChatMemory` object using `MessageWindowChatMemory.withMaxMessages(10);` and pass it to the `chatMemory` method. This implementation sends 
to the LLM the last 10 messages along with your current prompt. You can read more [here](https://docs.langchain4j.dev/tutorials/chat-memory#eviction-policy).

Feel free to test it by running the same command as in 3.4.

## 4. Deep dive on Prompt Roles

Prompting is a powerful feature that allows you to provide more context to the LLM, with the objective of reducing the hallucinations and making the responses more accurate.

In previous examples, the prompt contained only a simple message which is internally modelled as a "User Message" - this is called `user role`, meaning that the LLM perceives this message as coming directly from the user.

A prompt can have other roles, as follows:

* System Role: Guides the AI’s behavior and response style, setting parameters or rules for how the AI interprets and replies to the input. It’s akin to providing instructions to the AI before initiating a conversation.
* User Role: Represents the user’s input – their questions, commands, or statements to the AI. This role is fundamental as it forms the basis of the AI’s response.
* Assistant Role: The AI’s response to the user’s input. More than just an answer or reaction, it’s crucial for maintaining the flow of the conversation. By tracking the AI’s previous responses (its 'Assistant Role' messages), the system ensures coherent and contextually relevant interactions.
* Function Role: This role deals with specific tasks or operations during the conversation. While the System Role sets the AI’s overall behavior, the Function Role focuses on carrying out certain actions or commands the user asks for. It’s like a special feature in the AI, used when needed to perform specific functions such as calculations, fetching data, or other tasks beyond just talking. This role allows the AI to offer practical help in addition to conversational responses.


All the code we explore in this step is in 2 files:
* LangchainPromptController.java
* LangchainPromptService.java


### 4.1 Set up an UserMessage object

In the `LangchainPromptService.java` file, update the `call` method by creating a UserMessage object from our query.

This message has a `user` role.

Hint: we're using Java 17, so you can use the `var` keyword to keep the code more concise.

<details>
  <summary>Solution here</summary>

    var userMessage = UserMessage.from(message);

</details>



### 4.2 Create a message with a System role

We're going to create a new message with a System role now.
To do that, we're going to take the system prompt from a file located in `src/main/resources/prompts/system-message.st` file.

Explore that file and notice that it contains some placeholders.

Coming back to `LangchainPromptService`, notice that we already read that file's content in `systemPromptText`. Use the `replace` method to replace the placeholders with the actual values.

Then create a `SystemMessage` which wraps the system prompt text.

<details>
  <summary>Solution here</summary>

    var systemPromptText = systemResource.getContentAsString(Charsets.UTF_8)
        .replace("{name}", name)
        .replace("{voice}", voice);
    SystemMessage systemMessage = SystemMessage.from(systemPromptText);

</details>


### 4.3 Pass the system and the user messages to the LLM in a single call

<details>
  <summary>Solution here</summary>

    return chatLanguageModel.generate(systemMessage, userMessage).content().text();

</details>


Once you've done that, make sure you update the `LangchainPromptController` to call the service, and then test everything by calling:
``````shell
curl localhost:8080/langhcain/generate/prompt
``````

## 5. Exploring the Retrieval Augmented Generation use-case

In this step, we're going to explore the Retrieval Augmented Generation (RAG) use-case, which is a technique to improve the accuracy of the responses, by providing relevant information to the LLM, information that we pack into the System prompt.

How it works:
* We need to have some data to be used to improve the responses. This data can be stored in a file, a database, or any other source. For this workshop, we have the file stored in `/src/main/resources/data/incidents.json`
* Based on that data, we need create a vector store object, which is a special data-structure that allows us to search for similar documents based on a given query (similarity search).
* So when we have a query, we search in the vector store for the most similar documents, and then we add those into the system prompt, so the LLM can use that information to generate a more accurate response.

All the code we explore in this step is in 2 files:
* LangchainRagController.java
* LangchainRagService.java

The steps for RAG are similar to the ones we did during the Spring AI part. However Langchain4j has an "Easy RAG" feature that
is great for prototyping. You only have to provide Langchain4j the location of your documents without placing much thought 
into all the steps involved. You can find an explanation of how it works [here](https://docs.langchain4j.dev/tutorials/rag/#easy-rag).

### 5.1 Set up the AI Service

In `LangchainRagService` class, (retrieve method), create an AI Service for interacting with the LLM. When you define the interface,
use the `@SystemMessage` annotation to set the system message to:
```
You are ProvisioningHelper, an assistant helping provisioning team developers at Adobe to answer questions related to On-call incidents.
Provisioning team uses already solved Jira tickets and clients related information to find the root cause of the incidents.
Use the information from the DOCUMENTS section to provide accurate answers.
If you know the answer to the question, include the Jira ticket id in the response.
Separate each line in the response by a newline character '\n'.
If the question is not about an incident, politely ask the user to detail the question.
If you don't know the answer, say "Sorry, I don't know".
```

Also make sure to use `AiServices` to create an instance.

<details>
  <summary>Solution here</summary>

    interface Assistant {
        @SystemMessage("""
                You are ProvisioningHelper, an assistant helping provisioning team developers at Adobe to answer questions related to On-call incidents.
                Provisioning team uses already solved Jira tickets and clients related information to find the root cause of the incidents.
                Use the information from the DOCUMENTS section to provide accurate answers.
                If you know the answer to the question, include the Jira ticket id in the response.
                Separate each line in the response by a newline character '\n'.
                If the question is not about an incident, politely ask the user to detail the question.
                If you don't know the answer, say "Sorry, I don't know".
                """)
        String chat(String userMessage);
    }

    Assistant assistant = AiServices.builder(Assistant.class)
        .chatLanguageModel(chatLanguageModel)
        .build();

</details>

### 5.2 Load your documents

Get the absolute path of the `incidentsResource` and use `FileSystemDocumentLoader` to load the documents at that location.

<details>
  <summary>Solution here</summary>

    String path = incidentsResource.getFile().getParent();
    List<Document> documents = FileSystemDocumentLoader.loadDocuments(path);

</details>

### 5.3 Preprocess and add the documents to the vector store

For simplicity, we will use an in-memory vector store. Initialize an `InMemoryEmbeddingStore<TextSegment>` object and then 
call `EmbeddingStoreIngestor` to ingest the documents loaded earlier into the vector store.

<details>
  <summary>Solution here</summary>

    InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
    EmbeddingStoreIngestor.ingest(documents, embeddingStore);

</details>

### 5.4 Augment the AI Service to use RAG

When building your AIService, make it point to the vector store using the `contentRetriever` method.

<details>
  <summary>Solution here</summary>

        Assistant assistant = AiServices.builder(Assistant.class)
            .chatLanguageModel(chatLanguageModel)
            .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
            .build();
        return assistant.chat(message); 

</details>

Finally, do your queries as usual and test the app by calling:
``````shell
curl localhost:8080/langchain/generate/rag
``````
(Don't forget to update the `LangchainRagController` class to call the service)

<details>
  <summary>Solution here</summary>
    var systemMessage = getSystemMessage(similarDocuments);
    var userMessage = new UserMessage(message);

    var prompt = new Prompt(List.of(systemMessage, userMessage));

    return chatClient.call(prompt).getResult().getOutput();      
</details>

## 6. Exploring the Function Calling capability

In this step, we're going to explore the Function Calling capability, which allows you to instruct the LLM to "call a function" defined in your code.

The LLM itself doesn't call the function, since it's running in the cloud. What it does is to respond with a JSON payload that Langchain4j uses to call your function.

How does Langchain4j know which function to call? By placing the @Tool annotation on your function, providing a description and passing an object of the class that contains that function to the `AiServices` builder.
The library will internally convert your function to a tool specification that the LLM can understand.

All the code we explore in this step is in 4 files:
* LangchainFunctionController.java
* LangchainPaymentService.java
* LangchainPaymentConfig.java

### 6.1. What is the use-case?

We have a `LangchainPaymentConfig` which contains a list of transactions with their statuses (pending, approved, rejected). They're stored in an in-memory map for now, but they can be stored in a DB somewhere.

The user can ask the LLM to get the status of a transaction, by providing the transaction ID. The LLM is not trained to respond with such an information, so it will start looking for a function to call.

This is where we come in. We have a function `getStatusForTransaction` that can do that. All that's left to do is go to `LangchainPaymentService` and when creating the payment assistant use the `tools` method to 
pass in an instance  of the object containing that method.

You can then test as follows:

``````shell
  curl localhost:8080/generate/function
``````

<details>
  <summary>Solution here</summary>

    PaymentAssistant delegationAssistant = AiServices.builder(PaymentAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(new LangchainPaymentConfig())
                .build();
</details>

### 7.0 [EXTRA] Modify 3.1 to use [response streaming ](https://docs.langchain4j.dev/tutorials/response-streaming)
