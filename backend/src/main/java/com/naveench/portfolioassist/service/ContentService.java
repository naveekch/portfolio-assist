package com.naveench.portfolioassist.service;

import com.naveench.portfolioassist.content.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Thin lookup layer over {@link PortfolioContent}. Kept separate from the
 * @Tool methods in PortfolioTools so the matching logic (fuzzy-ish name
 * lookup) is unit-testable without spinning up Spring AI at all.
 */
@Service
public class ContentService {

    public Optional<ExperienceEntry> findExperience(String companyQuery) {
        if (companyQuery == null || companyQuery.isBlank()) {
            return Optional.empty();
        }
        String q = normalize(companyQuery);
        return PortfolioContent.experience().stream()
                .filter(e -> normalize(e.company()).contains(q) || q.contains(normalize(e.company())))
                .findFirst();
    }

    public Optional<ProjectEntry> findProject(String nameQuery) {
        if (nameQuery == null || nameQuery.isBlank()) {
            return Optional.empty();
        }
        String q = normalize(nameQuery);
        return PortfolioContent.projects().stream()
                .filter(p -> normalize(p.name()).contains(q) || q.contains(normalize(p.name())))
                .findFirst();
    }

    public List<SkillGroup> skillsFor(String areaQuery) {
        if (areaQuery == null || areaQuery.isBlank()) {
            return PortfolioContent.skills();
        }
        String q = normalize(areaQuery);
        List<SkillGroup> matches = PortfolioContent.skills().stream()
                .filter(g -> normalize(g.area()).contains(q) || q.contains(normalize(g.area())))
                .toList();
        return matches.isEmpty() ? PortfolioContent.skills() : matches;
    }

    /** All skill strings across every group, flattened - used for JD matching. */
    public List<String> allSkillTerms() {
        return PortfolioContent.skills().stream()
                .flatMap(g -> g.items().stream())
                .toList();
    }

    private static String normalize(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}
