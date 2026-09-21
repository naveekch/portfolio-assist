package com.naveench.portfolioassist.mcp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Per-request tool call trace, the same idea as Bank Assist's ToolTrace: a
 * ThreadLocal list that PortfolioTools appends to, which the controller reads
 * back out after the agent finishes, so the front end can show "here's what I
 * actually looked up" instead of asking you to trust a paragraph of prose.
 */
public final class ToolTrace {

    public record Entry(String tool, Map<String, Object> args, String outcome, long latencyMs) {
    }

    private static final ThreadLocal<List<Entry>> CURRENT = ThreadLocal.withInitial(ArrayList::new);

    private ToolTrace() {
    }

    public static void begin() {
        CURRENT.set(new ArrayList<>());
    }

    public static void record(Entry entry) {
        CURRENT.get().add(entry);
    }

    public static List<Entry> current() {
        return List.copyOf(CURRENT.get());
    }

    public static void clear() {
        CURRENT.remove();
    }
}
