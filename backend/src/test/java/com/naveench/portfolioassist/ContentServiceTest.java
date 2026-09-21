package com.naveench.portfolioassist;

import com.naveench.portfolioassist.content.PortfolioContent;
import com.naveench.portfolioassist.service.ContentService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentServiceTest {

    private final ContentService service = new ContentService();

    @Test
    void findsExperienceCaseInsensitivePartial() {
        var e = service.findExperience("fidelity");
        assertTrue(e.isPresent());
        assertEquals("Fidelity Investments", e.get().company());
    }

    @Test
    void findsProjectByPartialName() {
        var p = service.findProject("bank assist");
        assertTrue(p.isPresent());
        assertEquals("Bank Assist", p.get().name());
    }

    @Test
    void unknownEmployerReturnsEmpty() {
        assertTrue(service.findExperience("NotARealCompany").isEmpty());
    }

    @Test
    void skillsForKnownAreaFilters() {
        var groups = service.skillsFor("testing");
        assertEquals(1, groups.size());
        assertTrue(groups.get(0).items().stream().anyMatch(s -> s.contains("Playwright")));
    }

    @Test
    void skillsForUnknownAreaFallsBackToEverything() {
        var groups = service.skillsFor("underwater basket weaving");
        assertEquals(PortfolioContent.skills().size(), groups.size());
    }

    @Test
    void allSkillTermsIsNonEmpty() {
        assertFalse(service.allSkillTerms().isEmpty());
    }
}
