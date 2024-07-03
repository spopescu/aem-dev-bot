# GenAI with Java workshop

This repository contains the support code for the GenAI with Java workshop.

## Spring AI + Langchain4j
In this workshop, you will learn how to use Spring AI and Langchain4j for the following tasks:
- Interact with an LLM (e.g., Azure OpenAI)
- Implement RAG (Retrieval Augmented Generation) on an LLM using your custom data
- Create a function callback that can be executed by the LLM

The `main` branch contains the starting code.

Please follow the comments marked with `TODO:` and try to implement the features.

If you are stuck or you have any question, please ask for help.

Solutions for each task are available in the following Github branches:
- `chat` - basic interaction with an LLM
- `prompt` - prompts customization when interacting with an LLM
- `rag` - minimal on-call assistant trained on custom data as an example of the RAG technique
- `function_calling` - custom function definition that can be executed by the LLM

## Setup

1. Clone this repository
2. Open the project in IntelliJ IDEA
2. Make sure you have Maven 3.8+ and Java 17 installed by running `mvn --version` and `java -version`
3. Build the project by running the `mvn clean install` command in the terminal
4. Add your `API_KEY` in the `application.properties` file
5. Run the application
6. Send requests to `localhost:8080`(change the endpoint according to the task you are working on)
