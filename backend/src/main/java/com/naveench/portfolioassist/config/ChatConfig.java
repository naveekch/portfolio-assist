package com.naveench.portfolioassist.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Backed by the Gemini Developer API (a free Google AI Studio key, not
 * Vertex AI / GCP billing) via GoogleGenAiChatModel. Swapping model
 * providers is exactly this one bean - the tool loop, guardrails and
 * everything else in AgentService/PortfolioTools are provider-agnostic.
 */
@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(GoogleGenAiChatModel model) {
        return ChatClient.builder(model).build();
    }
}
