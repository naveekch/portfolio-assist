package com.naveench.portfolioassist.dto;

/**
 * One answer produced with no tools and no grounding rules, used by the UI to
 * show what a given reply would have looked like without the guardrails.
 */
public record UngroundedResponse(String answer, long latencyMs) {
}
