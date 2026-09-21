package com.naveench.portfolioassist.content;

import java.util.List;

/**
 * A side/portfolio project - the AI projects, not the day-job work (that's ExperienceEntry).
 *
 * @param name        display name, e.g. "Bank Assist"
 * @param tagline     short subtitle shown under the name, e.g. "Spring Boot + MCP + Angular"
 * @param summary     one or two sentences for the compact "listProjects" view
 * @param detail      full paragraph(s) for "getProjectDetail"
 * @param url         GitHub/README/live link
 * @param stack       technologies used (for skill matching)
 */
public record ProjectEntry(
        String name,
        String tagline,
        String summary,
        String detail,
        String url,
        List<String> stack
) {
}
