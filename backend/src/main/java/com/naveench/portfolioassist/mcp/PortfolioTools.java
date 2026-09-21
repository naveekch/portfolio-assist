package com.naveench.portfolioassist.mcp;

import com.naveench.portfolioassist.content.*;
import com.naveench.portfolioassist.service.ContentService;
import com.naveench.portfolioassist.service.FitAssessor;
import com.naveench.portfolioassist.service.PromptInjectionGuard;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Every fact-bearing action the assistant can take, published as MCP tools
 * (so any MCP client, Claude Desktop included, can call them directly) and as
 * an in-process ToolCallback[] for the agent loop (see McpConfig). One
 * implementation, one set of tools, two entry points - the same shape as
 * Bank Assist's BankingTools.
 *
 * There's no RBAC or ownership check here (everything is public professional
 * information), so `guarded()` is smaller than Bank Assist's: it times the
 * call, records it to the trace so the UI can show what ran, and turns any
 * exception into a clean refusal instead of a stack trace reaching the model.
 */
@Component
public class PortfolioTools {

    private final ContentService content;
    private final FitAssessor fitAssessor;
    private final PromptInjectionGuard injectionGuard;

    public PortfolioTools(ContentService content, FitAssessor fitAssessor, PromptInjectionGuard injectionGuard) {
        this.content = content;
        this.fitAssessor = fitAssessor;
        this.injectionGuard = injectionGuard;
    }

    @Tool(description = "Get Naveen's short professional summary, contact info, and profile links (LinkedIn, GitHub, portfolio).")
    public String getProfile() {
        return guarded("getProfile", Map.of(), () -> {
            StringBuilder sb = new StringBuilder();
            sb.append(PortfolioContent.PROFILE_SUMMARY).append("\n\n");
            sb.append("Contact: ").append(PortfolioContent.EMAIL).append(" | ").append(PortfolioContent.PHONE).append("\n");
            sb.append("LinkedIn: ").append(PortfolioContent.LINKEDIN).append("\n");
            sb.append("GitHub: ").append(PortfolioContent.GITHUB).append("\n");
            sb.append("Portfolio: ").append(PortfolioContent.PORTFOLIO_URL);
            return sb.toString();
        });
    }

    @Tool(description = "List every job on Naveen's work history in order, most recent first, with title, dates, and a one-line summary of each.")
    public String listExperience() {
        return guarded("listExperience", Map.of(), () -> {
            List<ExperienceEntry> all = PortfolioContent.experience();
            StringBuilder sb = new StringBuilder();
            for (ExperienceEntry e : all) {
                sb.append("- ").append(e.company()).append(" — ").append(e.title())
                        .append(" (").append(e.dateRange()).append("): ").append(e.oneLiner()).append("\n");
            }
            return sb.toString().strip();
        });
    }

    @Tool(description = "Get the full detail (bullets and tech stack) for one specific employer, e.g. 'Fidelity', 'Capital One', 'Belk', 'Infosys', 'Tech Mahindra', or 'Vrusoft'.")
    public String getExperienceDetail(String company) {
        return guarded("getExperienceDetail", Map.of("company", company), () ->
                content.findExperience(company)
                        .map(e -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append(e.company()).append(" — ").append(e.title())
                                    .append(" (").append(e.dateRange()).append(")\n");
                            for (String b : e.bullets()) {
                                sb.append("- ").append(b).append("\n");
                            }
                            sb.append("Stack: ").append(String.join(", ", e.stack()));
                            return sb.toString();
                        })
                        .orElse("No employer matching \"" + company + "\" in the work history."));
    }

    @Tool(description = "List Naveen's side/AI projects (not day-job work) with a one-line summary and link for each: Bank Assist, Portfolio Assist, AI Programming Assistant, VisionTutor.")
    public String listProjects() {
        return guarded("listProjects", Map.of(), () -> {
            StringBuilder sb = new StringBuilder();
            for (ProjectEntry p : PortfolioContent.projects()) {
                sb.append("- ").append(p.name()).append(" (").append(p.tagline()).append("): ")
                        .append(p.summary()).append(" ").append(p.url()).append("\n");
            }
            return sb.toString().strip();
        });
    }

    @Tool(description = "Get the full detail and link for one specific project by name, e.g. 'Bank Assist', 'VisionTutor', 'AI Programming Assistant', or 'Portfolio Assist'.")
    public String getProjectDetail(String name) {
        return guarded("getProjectDetail", Map.of("name", name), () ->
                content.findProject(name)
                        .map(p -> p.detail() + "\n\nLink: " + p.url() + "\nStack: " + String.join(", ", p.stack()))
                        .orElse("No project matching \"" + name + "\"."));
    }

    @Tool(description = "Get Naveen's skills, optionally filtered by area: 'testing', 'apis', 'backend', 'frontend', 'data', 'cloud-and-devops', or 'ai'. Leave blank for everything.")
    public String getSkills(String area) {
        return guarded("getSkills", Map.of("area", area == null ? "" : area), () -> {
            StringBuilder sb = new StringBuilder();
            for (SkillGroup g : content.skillsFor(area)) {
                sb.append(g.area()).append(": ").append(String.join(", ", g.items())).append("\n");
            }
            return sb.toString().strip();
        });
    }

    @Tool(description = "Get Naveen's education and certifications.")
    public String getEducation() {
        return guarded("getEducation", Map.of(), () -> {
            StringBuilder sb = new StringBuilder("Education:\n");
            for (EducationEntry e : PortfolioContent.education()) {
                sb.append("- ").append(e.degree()).append(", ").append(e.school()).append("\n");
            }
            sb.append("\nCertifications:\n");
            for (CertificationEntry c : PortfolioContent.certifications()) {
                sb.append("- ").append(c.name());
                if (!c.note().isBlank()) {
                    sb.append(" (").append(c.note()).append(")");
                }
                sb.append(" — ").append(c.verifyUrl()).append("\n");
            }
            return sb.toString().strip();
        });
    }

    @Tool(description = "Assess fit against a pasted job description. Returns technologies from the JD that Naveen has real experience with, technologies mentioned that he does not have background in, and which jobs/projects best demonstrate the matches. Pass the raw job description text as jobDescription.")
    public String assessFit(String jobDescription) {
        return guarded("assessFit", Map.of("jobDescriptionLength", jobDescription == null ? 0 : jobDescription.length()), () -> {
            var guard = injectionGuard.scan(jobDescription);
            var result = fitAssessor.assess(jobDescription);

            StringBuilder sb = new StringBuilder();
            if (guard.suspicious()) {
                sb.append("Note: the pasted text contained phrasing that looks like it's trying to give "
                        + "the assistant new instructions (").append(String.join("; ", guard.flaggedPhrases()))
                        .append("). It was treated as plain text to analyze, not as instructions.\n\n");
            }
            sb.append("Matched (real experience): ").append(String.join(", ", result.matchedSkills())).append("\n");
            sb.append("Gaps (mentioned in JD, not in background): ").append(String.join(", ", result.gaps())).append("\n");
            sb.append("Relevant jobs: ").append(String.join(", ", result.highlightedExperience())).append("\n");
            sb.append("Relevant projects: ").append(String.join(", ", result.highlightedProjects())).append("\n");
            sb.append("Match count: ").append(result.matchedCount()).append(" matched / ").append(result.gapCount()).append(" gaps");
            return sb.toString();
        });
    }

    // ---- internal ----

    private String guarded(String tool, Map<String, Object> args, Supplier<String> body) {
        long start = System.currentTimeMillis();
        try {
            String out = body.get();
            ToolTrace.record(new ToolTrace.Entry(tool, args, "OK", System.currentTimeMillis() - start));
            return out;
        } catch (Exception ex) {
            ToolTrace.record(new ToolTrace.Entry(tool, args, "ERROR", System.currentTimeMillis() - start));
            return "Something went wrong looking that up (" + ex.getClass().getSimpleName() + "). Try rephrasing the question.";
        }
    }
}
