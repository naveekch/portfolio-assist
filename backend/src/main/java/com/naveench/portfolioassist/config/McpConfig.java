package com.naveench.portfolioassist.config;

import com.naveench.portfolioassist.mcp.PortfolioTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers PortfolioTools twice from one definition, same pattern as
 * Bank Assist's McpConfig: once as a ToolCallbackProvider for the MCP
 * transport (so Claude Desktop or any MCP client can call these tools
 * directly), and once as a plain ToolCallback[] for the in-process agent
 * loop used by AgentService.
 */
@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider portfolioToolCallbackProvider(PortfolioTools portfolioTools) {
        return MethodToolCallbackProvider.builder().toolObjects(portfolioTools).build();
    }

    @Bean
    public ToolCallback[] portfolioToolCallbacks(PortfolioTools portfolioTools) {
        return ToolCallbacks.from(portfolioTools);
    }
}
