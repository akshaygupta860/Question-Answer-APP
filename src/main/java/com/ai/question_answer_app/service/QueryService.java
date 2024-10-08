package com.ai.question_answer_app.service;

import org.jsoup.Jsoup;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueryService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public QueryService(VectorStore vectorStore, ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    public String getQuery(String queryInput) {

         final String template = """
                        
            You're assisting with questions about services offered to filter CV's .
                    
            Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
            If unsure, simply state that you don't know.
                    
            DOCUMENTS:
            {documents}
                        
            """;

       /*List<Document> results = vectorStore
                .similaritySearch(SearchRequest.query(queryInput).withTopK(1));*/

        var listOfSimilarDocuments = this.vectorStore.similaritySearch(queryInput);
        var documents = listOfSimilarDocuments
                .stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));
        var systemMessage = new SystemPromptTemplate(template)
                .createMessage(Map.of("documents", documents));
        var userMessage = new UserMessage(queryInput);
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        var aiResponse = chatClient.prompt(prompt).call().content();
        return aiResponse;

    }


    public void importDataThroughURL(String url) throws IOException {

        org.jsoup.nodes.Document body = Jsoup.connect(url).get();
      //  String text = body.getTex();

        System.out.println(body);




           // vectorStore.add(document);

    }
}
