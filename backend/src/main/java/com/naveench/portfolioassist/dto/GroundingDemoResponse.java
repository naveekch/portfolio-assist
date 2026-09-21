package com.naveench.portfolioassist.dto;

import java.util.List;

/**
 * Both answers to the same question: one where the model can call tools and
 * one where it can't.
 *
 * The ungrounded side is the honest control, not a strawman - it gets the same
 * persona prompt, just without the tool access and the "call a tool before you
 * state a fact" rules. That is exactly what this project would be if it were
 * the usual "chat with my resume" wrapper, which is the point of showing them
 * side by side.
 */
public record GroundingDemoResponse(
        String question,
        String groundedAnswer,
        List<ToolCallView> groundedToolCalls,
        long groundedLatencyMs,
        String ungroundedAnswer,
        long ungroundedLatencyMs
) {
}
