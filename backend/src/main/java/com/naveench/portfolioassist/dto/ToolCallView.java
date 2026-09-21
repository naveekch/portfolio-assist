package com.naveench.portfolioassist.dto;

import java.util.Map;

public record ToolCallView(String tool, Map<String, Object> args, String outcome, long latencyMs) {
}
