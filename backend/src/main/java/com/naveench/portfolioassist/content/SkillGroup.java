package com.naveench.portfolioassist.content;

import java.util.List;

/**
 * A named cluster of skills, e.g. area="testing", items=["Playwright", "Selenium", ...].
 * Grouping (rather than one flat list) is what lets getSkills(area) answer
 * "what's your testing stack" without dumping everything.
 */
public record SkillGroup(String area, List<String> items) {
}
