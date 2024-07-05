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

## Setup Bots

1. Install and start Flowise (follow instruction from https://github.com/FlowiseAI/Flowise)
2. Download ollama (https://ollama.com/download/mac or follow https://github.com/ollama/ollama) and start it (either starting the Application itself or running `ollama serve`. By default it will start on port 11434) 
3. Start codellama model using the ollama environment: `ollama run codellama:7b`
4. In the browser, access the Flowise UI at `localhost:3000` and import the 2 chatflows from the `/resources` folder.
5. Access each of the 2 created chatflows
 - 5.1 In the UI, link the documentation files (ex: `sling.txt`) to the respective bot embedding document.
 - 5.2 Get the endpoints for each of them (should be similar to `http://localhost:3000/api/v1/prediction/fd267914-6759-47c1-89bd-e897b1d1b175`)
6. Complete the `application.properties` file with the 2 URLs for each bot. We have now linked each Spring Bot Bean to their running Bot instances. 

Note that the Bots use the same running model, but 2 different vector stores for retrieving the embeddings. It is the Flowise app that adds an abstraction layer and wraps the calls to the model itself, so we can treat each Bot as a standalone service that just retrieves a question.

The **Master App** is now able to make API calls to the Bots.
