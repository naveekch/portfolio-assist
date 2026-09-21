package com.naveench.portfolioassist.service;

import com.naveench.portfolioassist.content.ExperienceEntry;
import com.naveench.portfolioassist.content.PortfolioContent;
import com.naveench.portfolioassist.content.ProjectEntry;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Deterministic Java behind the "does this JD fit me" feature, on the same
 * principle Bank Assist uses for money: the model must not eyeball a skill
 * match and it must not be trusted to remember what's real. This class scans
 * the pasted text against a fixed dictionary of technology terms, derives
 * "known" (Naveen has it, checked against real content) vs "gap" (mentioned
 * in the JD but not in the content) mechanically, and hands the model only
 * the resulting lists to narrate - it can't claim a match that isn't here.
 */
@Service
public class FitAssessor {

    /** label -> aliases to search for in the JD text (case-insensitive substring match). */
    private static final Map<String, List<String>> TECH_DICTIONARY = buildDictionary();

    public record FitResult(
            List<String> matchedSkills,
            List<String> gaps,
            List<String> highlightedExperience,
            List<String> highlightedProjects,
            int matchedCount,
            int gapCount
    ) {
    }

    public FitResult assess(String jdText) {
        String normalized = jdText == null ? "" : jdText.toLowerCase();
        // All of Naveen's real skill/stack terms, lowercased and joined with a
        // delimiter that can never be a word character, so "\bjava\b" can be
        // tested against it the same way it's tested against the JD text.
        String ownedBlob = String.join(" ~ ", ownedTermsLowercase());

        List<String> matched = new ArrayList<>();
        List<String> gaps = new ArrayList<>();

        for (var entry : TECH_DICTIONARY.entrySet()) {
            String label = entry.getKey();
            boolean mentionedInJd = entry.getValue().stream().anyMatch(alias -> containsWord(normalized, alias));
            if (!mentionedInJd) {
                continue;
            }
            boolean owned = entry.getValue().stream().anyMatch(alias -> containsWord(ownedBlob, alias));
            if (owned) {
                matched.add(label);
            } else {
                gaps.add(label);
            }
        }

        // Highlighting has to key off the actual matched ALIASES, not the
        // (more descriptive) dictionary label - "MCP (Model Context Protocol)"
        // would never equal or contain a plain stack entry like "MCP", so
        // comparing against the label made this silently miss real matches.
        Set<String> matchedAliasesFlat = matched.stream()
                .flatMap(label -> TECH_DICTIONARY.get(label).stream())
                .collect(Collectors.toSet());

        List<String> highlightedExperience = PortfolioContent.experience().stream()
                .filter(e -> e.stack().stream().anyMatch(s -> matchedAliasesFlat.stream()
                        .anyMatch(alias -> containsWord(s.toLowerCase(), alias) || containsWord(alias, s.toLowerCase()))))
                .map(ExperienceEntry::company)
                .distinct()
                .toList();

        List<String> highlightedProjects = PortfolioContent.projects().stream()
                .filter(p -> p.stack().stream().anyMatch(s -> matchedAliasesFlat.stream()
                        .anyMatch(alias -> containsWord(s.toLowerCase(), alias) || containsWord(alias, s.toLowerCase()))))
                .map(ProjectEntry::name)
                .distinct()
                .toList();

        return new FitResult(matched, gaps, highlightedExperience, highlightedProjects,
                matched.size(), gaps.size());
    }

    /**
     * Whole-word, case-insensitive containment. Plain String.contains would let
     * the alias "java" match inside "javascript", or "git" match inside
     * "gitlab" - both real false positives with a résumé/JD-scanning dictionary
     * this size. Word boundaries fix that while still matching "REST," or
     * "(MCP)" correctly, since punctuation counts as a boundary too.
     */
    private static boolean containsWord(String haystackLower, String alias) {
        String pattern = "\\b" + Pattern.quote(alias.trim().toLowerCase()) + "\\b";
        return Pattern.compile(pattern).matcher(haystackLower).find();
    }

    private Set<String> ownedTermsLowercase() {
        Set<String> terms = new HashSet<>();
        for (var g : PortfolioContent.skills()) {
            for (String s : g.items()) {
                terms.add(s.toLowerCase());
            }
        }
        for (var e : PortfolioContent.experience()) {
            for (String s : e.stack()) {
                terms.add(s.toLowerCase());
            }
        }
        for (var p : PortfolioContent.projects()) {
            for (String s : p.stack()) {
                terms.add(s.toLowerCase());
            }
        }
        return terms;
    }

    private static Map<String, List<String>> buildDictionary() {
        Map<String, List<String>> d = new LinkedHashMap<>();
        d.put("Java", List.of("java"));
        d.put("Spring Boot", List.of("spring boot", "springboot"));
        d.put("Spring (Core/IOC/JDBC/ORM)", List.of("spring core", "spring ioc", "spring jdbc", "spring orm", "spring framework"));
        d.put("Spring AI", List.of("spring ai"));
        d.put("Hibernate", List.of("hibernate"));
        d.put("JDBC", List.of("jdbc"));
        d.put("JSP", List.of("jsp"));
        d.put("Servlets", List.of("servlet"));
        d.put("Struts", List.of("struts"));
        d.put("Quarkus", List.of("quarkus"));
        d.put("Microservices", List.of("microservice"));
        d.put("Python", List.of("python"));
        d.put("Node.js", List.of("node.js", "nodejs", "node js"));
        d.put("JavaScript", List.of("javascript"));
        d.put("TypeScript", List.of("typescript"));
        d.put("Angular", List.of("angular"));
        d.put("React", List.of("react"));
        d.put("jQuery", List.of("jquery"));
        d.put("HTML5/CSS3", List.of("html5", "css3", "html", "css"));
        d.put("AWS", List.of("aws", "amazon web services"));
        d.put("Azure", List.of("azure"));
        d.put("GCP", List.of("gcp", "google cloud"));
        d.put("Docker", List.of("docker"));
        d.put("Kubernetes", List.of("kubernetes", "k8s"));
        d.put("Terraform", List.of("terraform"));
        d.put("Jenkins", List.of("jenkins"));
        d.put("GitHub Actions", List.of("github actions"));
        d.put("Git", List.of("git"));
        d.put("GitLab", List.of("gitlab"));
        d.put("Azure Git", List.of("azure git", "azure devops"));
        d.put("Maven", List.of("maven"));
        d.put("Playwright", List.of("playwright"));
        d.put("Selenium", List.of("selenium"));
        d.put("Cypress", List.of("cypress"));
        d.put("Cucumber/BDD", List.of("cucumber", "bdd"));
        d.put("JUnit", List.of("junit"));
        d.put("TestNG", List.of("testng"));
        d.put("Postman", List.of("postman"));
        d.put("Swagger", List.of("swagger"));
        d.put("REST/RESTful", List.of("restful", "rest api", "rest"));
        d.put("SOAP", List.of("soap"));
        d.put("WSDL", List.of("wsdl"));
        d.put("JAX-WS", List.of("jax-ws", "jax ws"));
        d.put("Apache Axis", List.of("apache axis"));
        d.put("JAX-RS (Jersey)", List.of("jax-rs", "jersey"));
        d.put("GraphQL", List.of("graphql"));
        d.put("gRPC", List.of("grpc"));
        d.put("Apache Tomcat", List.of("tomcat"));
        d.put("IBM WebSphere", List.of("websphere"));
        d.put("Oracle WebLogic", List.of("weblogic"));
        d.put("Eclipse IDE", List.of("eclipse"));
        d.put("IntelliJ", List.of("intellij"));
        d.put("Rally", List.of("rally"));
        d.put("Jira", List.of("jira"));
        d.put("Oracle DB", List.of("oracle db", "oracle database", "oracle"));
        d.put("MySQL", List.of("mysql"));
        d.put("PostgreSQL", List.of("postgres", "postgresql"));
        d.put("DynamoDB", List.of("dynamodb"));
        d.put("MongoDB", List.of("mongodb"));
        d.put("Cassandra", List.of("cassandra"));
        d.put("Snowflake", List.of("snowflake"));
        d.put("Redis", List.of("redis"));
        d.put("Kafka", List.of("kafka"));
        d.put("RabbitMQ", List.of("rabbitmq"));
        d.put("Salesforce", List.of("salesforce"));
        d.put("MCP (Model Context Protocol)", List.of("mcp", "model context protocol"));
        d.put("Agentic AI", List.of("agentic ai", "agentic", "ai agent"));
        d.put("GitHub Copilot", List.of("copilot"));
        d.put("Claude", List.of("claude"));
        d.put("OpenAI/ChatGPT", List.of("openai", "chatgpt", "gpt-4", "gpt4"));
        d.put("Google Gemini", List.of("gemini"));
        return d;
    }
}
