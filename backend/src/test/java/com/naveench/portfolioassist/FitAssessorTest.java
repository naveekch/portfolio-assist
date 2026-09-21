package com.naveench.portfolioassist;

import com.naveench.portfolioassist.service.FitAssessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FitAssessorTest {

    private final FitAssessor assessor = new FitAssessor();

    @Test
    void matchesSkillsActuallyInBackground() {
        var r = assessor.assess("Looking for someone strong in Java, Spring Boot, and Playwright.");
        assertTrue(r.matchedSkills().contains("Java"));
        assertTrue(r.matchedSkills().contains("Spring Boot"));
        assertTrue(r.matchedSkills().contains("Playwright"));
        assertFalse(r.gaps().contains("Java"));
    }

    @Test
    void flagsRealGapsInsteadOfHidingThem() {
        var r = assessor.assess("Must have Oracle WebLogic, Struts, and Quarkus experience.");
        assertTrue(r.gaps().contains("Oracle WebLogic"));
        assertTrue(r.gaps().contains("Struts"));
        assertTrue(r.gaps().contains("Quarkus"));
        assertFalse(r.matchedSkills().contains("Struts"));
    }

    @Test
    void highlightsExperienceThatUsedTheMatchedSkill() {
        var r = assessor.assess("Need Playwright automation experience.");
        assertTrue(r.highlightedExperience().contains("Fidelity Investments"));
    }

    @Test
    void matchesMcpAndAgenticAiAndHighlightsBankAssist() {
        var r = assessor.assess("Should have strong experience on Agentic AI, MCP.");
        assertTrue(r.matchedSkills().stream().anyMatch(s -> s.contains("MCP")));
        assertTrue(r.matchedSkills().stream().anyMatch(s -> s.contains("Agentic")));
        assertTrue(r.highlightedProjects().contains("Bank Assist"));
    }

    @Test
    void emptyJobDescriptionYieldsNoMatchesOrGaps() {
        var r = assessor.assess("");
        assertTrue(r.matchedSkills().isEmpty());
        assertTrue(r.gaps().isEmpty());
    }

    @Test
    void nullJobDescriptionDoesNotThrow() {
        var r = assessor.assess(null);
        assertEquals(0, r.matchedCount());
    }

    @Test
    void javaScriptMentionDoesNotFalselyMatchJava() {
        // Regression test: plain substring matching let "java" match inside
        // "javascript". A JD that only mentions JavaScript should not claim a
        // Java match.
        var r = assessor.assess("Strong JavaScript skills required.");
        assertFalse(r.matchedSkills().contains("Java"));
        assertTrue(r.matchedSkills().contains("JavaScript"));
    }

    @Test
    void gitHubMentionDoesNotFalselyMatchGit() {
        var r = assessor.assess("Experience with GitHub Actions required.");
        assertFalse(r.matchedSkills().contains("Git"));
    }

    @Test
    void matchesGeminiNowThatItsPartOfTheStack() {
        var r = assessor.assess("Experience with Google Gemini is a plus.");
        assertTrue(r.matchedSkills().contains("Google Gemini"));
    }

    @Test
    void highlightsBankAssistViaMcpAliasNotJustAgenticAiLabel() {
        // Regression test: highlighting used to key off the descriptive
        // dictionary label ("MCP (Model Context Protocol)"), which never
        // equals or contains the plain stack entry "MCP", so this match was
        // silently dropped unless another label happened to line up exactly.
        var r = assessor.assess("Must have MCP experience.");
        assertTrue(r.highlightedProjects().contains("Bank Assist"));
    }
}
