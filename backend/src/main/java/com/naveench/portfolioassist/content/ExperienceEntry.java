package com.naveench.portfolioassist.content;

import java.util.List;

/**
 * One job, told the way Naveen would tell it, not the way a resume bot would.
 *
 * @param company     display name of the employer
 * @param title       role title
 * @param location    city/state, or "Remote" / "" if not meaningful
 * @param start       e.g. "May 2023"
 * @param end         e.g. "Mar 2024", or "Present"
 * @param oneLiner    one sentence used in the compact "listExperience" view
 * @param bullets     the real detail, used in "getExperienceDetail"
 * @param stack       technologies actually used in this role (for skill matching)
 */
public record ExperienceEntry(
        String company,
        String title,
        String location,
        String start,
        String end,
        String oneLiner,
        List<String> bullets,
        List<String> stack
) {
    public String dateRange() {
        return start + " – " + end;
    }
}
