package com.naveench.portfolioassist.content;

import java.util.List;

/**
 * The single source of truth for everything the assistant is allowed to say about Naveen.
 *
 * This is deliberately plain, hand-maintained Java, not a database and not a vector
 * store. The whole point of the "no RAG" call for this project is that the content
 * is small and changes rarely enough that embedding it is solving a problem this
 * project doesn't have - when a new job or project shows up, it's a one-line edit
 * here, not a re-index.
 *
 * Every fact below comes from Naveen's actual resume content already in use
 * elsewhere (the master resume, the Truist-JD resume, and the Playwright-QA
 * resume). Nothing here is invented, and nothing should be added later without
 * the same grounding.
 */
public final class PortfolioContent {

    private PortfolioContent() {
    }

    public static final String NAME = "Naveen Chelluboina";
    public static final String EMAIL = "naveenkchelluboina@gmail.com";
    public static final String PHONE = "+1 940-758-3121";
    public static final String LINKEDIN = "https://www.linkedin.com/in/naveen-chelluboina/";
    public static final String GITHUB = "https://github.com/naveekch";
    public static final String PORTFOLIO_URL = "https://naveen-chelluboina-portfolio-master.onrender.com/";

    public static final String PROFILE_SUMMARY = """
            8+ years as a full stack engineer, mostly in financial services - Fidelity now, \
            Capital One before that. Most of the systems I've worked on were polyglot rather \
            than one language end to end: Spring Boot services sitting next to Python Lambdas \
            and Node APIs, with Angular or React in front, so I've gotten comfortable picking \
            up whatever a service is actually written in instead of waiting for a Java-only \
            project. Backend is the centre of it - Spring Boot microservices, Spring Core and \
            IOC, Hibernate and Spring Data JPA, JDBC and web services - with Maven and Jenkins \
            to build and ship. I've been building with LLMs for a while now: two products of my \
            own running on Claude and OpenAI with requests routed across models and a fallback \
            for when a provider drops, and earlier at Belk a prompt-driven Spring Boot platform \
            that moved events between Slack, Box and Wrike through pluggable adapters. Bank \
            Assist is the one I'd point at first - a Spring Boot MCP server I built to work out \
            how you let an AI agent near banking operations without it making things up or doing \
            something it shouldn't. If something on a project is new to me I go figure it out \
            rather than hand it back; a lot of what I'm strongest at now started exactly that way.""";

    public static List<ExperienceEntry> experience() {
        return List.of(
                new ExperienceEntry(
                        "Fidelity Investments", "Software Engineer", "", "Apr 2025", "Present",
                        "Advisor scheduling platform for Workplace Investing - Salesforce on top, "
                                + "Java microservices underneath.",
                        List.of(
                                "Replaced the old ad-hoc Outlook booking process with a proper Salesforce "
                                        + "Scheduler setup, and wrote the Lightning Web Components and Apex services "
                                        + "advisors use to publish their 1:1 and 1:many slots. Setup time dropped "
                                        + "roughly 40%.",
                                "The edit workflow was the tricky part. Once a meeting is published certain "
                                        + "fields have to lock, skills and availability still need checking, and "
                                        + "there's a named-advisor override sitting on top of all that. Built it as a "
                                        + "rule engine in Java and Apex.",
                                "Meeting types are metadata-driven, so the business team configures new "
                                        + "offerings themselves instead of filing a ticket and waiting for the next "
                                        + "release.",
                                "Advisor recommendations score on skills, current utilization and how close the "
                                        + "advisor is geographically. Comes back to the UI in under 150ms.",
                                "Most of the external integration work too: registration portals and virtual "
                                        + "meeting providers over REST, JWT and OAuth2 for auth, event queues for "
                                        + "publishing slots and writing registrations back.",
                                "Set up the SFDX and Maven pipelines with static analysis and automated tests. "
                                        + "Coverage sits at 90% now and a production deploy takes about 10 minutes."
                        ),
                        List.of("Java", "Spring Boot", "Salesforce", "Apex", "Lightning Web Components",
                                "REST", "JWT", "OAuth2", "Maven", "Jenkins", "Playwright", "TypeScript")
                ),
                new ExperienceEntry(
                        "Capital One Software", "Senior Software Engineer", "", "Apr 2024", "Mar 2025",
                        "Slingshot, Capital One's first SaaS product - helping customers keep Snowflake "
                                + "spend under control through cost monitoring, usage insights and alerts.",
                        List.of(
                                "Biggest piece of work was consolidating a sprawl of repositories into a mono "
                                        + "repo, pulling the gRPC and tRPC Java services into one Node.js and React "
                                        + "stack and moving the APIs over to Node REST.",
                                "Built the warehouse management UI, so customers create, scale and suspend "
                                        + "their Snowflake warehouses from inside Slingshot. Also the alerting side, "
                                        + "where they set thresholds and hear about a problem before the bill does.",
                                "Customer onboarding was mine end to end. The flow for connecting a Snowflake "
                                        + "account or data share, plus the backend that pulls those shared datasets "
                                        + "into customer-scoped databases.",
                                "Stood up a secondary stack in the west region for failover, with Route 53 "
                                        + "health checks and ingress routed through API Gateway, NLB and ECS so we "
                                        + "could cut over cleanly during an outage.",
                                "Smaller things: a Python Lambda that tears down every warehouse when a client "
                                        + "offboards, Terraform for the AWS provisioning, org templates and access "
                                        + "permissions in DynamoDB."
                        ),
                        List.of("Java", "Node.js", "React", "Python", "gRPC", "REST", "Snowflake",
                                "AWS", "Terraform", "Docker", "DynamoDB", "Playwright")
                ),
                new ExperienceEntry(
                        "Belk", "Software Developer", "", "May 2023", "Mar 2024",
                        "E-commerce platform redesign - checkout, product listings, Belk Rewards and "
                                + "gift cards - plus a prompt-driven Spring Boot integration platform.",
                        List.of(
                                "Built a prompt-driven integration platform inside Slack on Spring Boot. "
                                        + "Applications registered themselves through a subscription model, and adapter "
                                        + "modules handled the mapping between our events and whatever external API sat "
                                        + "on the other end, so adding Box or Wrike did not mean writing a fresh "
                                        + "integration each time.",
                                "Owned the backend APIs in Java and Spring Boot for checkout, listings and the "
                                        + "rewards and gift card features, along with the third-party integrations: "
                                        + "Loqate for address validation, card utils, Afterpay.",
                                "REST microservices with Spring Boot throughout. Spring Core annotations for "
                                        + "dependency injection, Spring MVC on the API layer, Spring Batch once the "
                                        + "data loads got large. Zipkin for tracing, Swagger for the docs.",
                                "Front end was React, mostly the product pages with the color and size pickers.",
                                "Did a fair bit of data work as well, Python and PySpark pipelines for inventory "
                                        + "analytics and some PyTorch models for stock optimization."
                        ),
                        List.of("Java", "Spring Boot", "Spring MVC", "Spring Batch", "REST", "React",
                                "Python", "PySpark", "Swagger", "Zipkin", "Playwright", "Selenium")
                ),
                new ExperienceEntry(
                        "Infosys", "Technology Analyst", "Dallas, TX", "Nov 2019", "Aug 2021",
                        "Identity microservices for Citi Bank's online banking application, built on "
                                + "Spring Boot.",
                        List.of(
                                "Identity microservices for Citi Bank's online banking application, built on "
                                        + "Spring Boot with REST, Swagger, Cassandra and MQ messaging underneath.",
                                "Hibernate and Spring Data JPA for the DAO layer, JDBC connection pooling, and "
                                        + "a good amount of hand-written SQL against DB2.",
                                "Wrote a Slack proxy with reactive endpoints in Spring Boot covering app "
                                        + "onboarding, channel creation, archiving and invites.",
                                "Jenkins for CI/CD, Bitbucket, JIRA, and Docker images for the Spring Boot "
                                        + "services."
                        ),
                        List.of("Java", "Spring Boot", "Hibernate", "Spring Data JPA", "JDBC", "SQL",
                                "DB2", "Cassandra", "JMS", "REST", "Swagger", "Jenkins", "Docker",
                                "Selenium", "TestNG")
                ),
                new ExperienceEntry(
                        "Tech Mahindra", "Java Developer", "", "May 2017", "Nov 2019",
                        "GSK's Master Template, an internal tool other developers used to build web "
                                + "templates, email templates and e-detailers.",
                        List.of(
                                "Rebuilt the whole thing from scratch in Angular 5 during the migration off "
                                        + "the old stack.",
                                "Built the customer self-service module end to end, Node.js with an Angular 6 "
                                        + "front end talking to REST services. Wrote a lot of custom Angular pieces "
                                        + "along the way, decorators, directives and pipes, with RxJS observables "
                                        + "handling the HTTP.",
                                "Java and JSP work on the server side of the template tool, deployed onto "
                                        + "Tomcat."
                        ),
                        List.of("Java", "JSP", "Angular", "Node.js", "REST", "RxJS", "Tomcat", "Selenium")
                ),
                new ExperienceEntry(
                        "Vrusoft Pvt. Ltd.", "Associate Developer", "Hyderabad, India", "May 2016", "May 2017",
                        "First job out of school - UI work and Node.js provisioning scripts.",
                        List.of(
                                "UI work in HTML5, CSS3, JavaScript and jQuery, with application code talking "
                                        + "to REST services over AJAX and JSON.",
                                "Wrote a Node.js provisioning script that pulled files off SFTP to create LDAP "
                                        + "and database users."
                        ),
                        List.of("HTML5", "CSS3", "JavaScript", "jQuery", "Node.js", "REST", "Selenium")
                )
        );
    }

    public static List<ProjectEntry> projects() {
        return List.of(
                new ProjectEntry(
                        "Bank Assist",
                        "Spring Boot + MCP + Angular",
                        "A retail banking assistant built as a Spring Boot MCP server, to work out "
                                + "how you safely let an AI agent near banking operations.",
                        "Retail banking assistant built as a Spring Boot MCP server with an agent "
                                + "layer over it. Banking operations are published as Model Context "
                                + "Protocol tools, so the same server drives both a chat UI and any MCP "
                                + "client, including Claude Desktop. The interesting part is the guardrails: "
                                + "the model never does arithmetic (Java totals integer cents, which is what "
                                + "stops a balance getting hallucinated), role permissions are a Java gate "
                                + "ahead of the tool body rather than a line in a prompt, writes need a "
                                + "confirmation round trip, every call lands in an audit table including "
                                + "denials, and PII is stripped before anything reaches the model. Java 21, "
                                + "Spring AI, JPA, Angular standalone components.",
                        "https://github.com/naveekch/banking-mcp-assistant#bank-assist",
                        List.of("Java", "Spring Boot", "Spring AI", "MCP", "Agentic AI", "JPA",
                                "PostgreSQL", "Angular", "TypeScript")
                ),
                new ProjectEntry(
                        "Portfolio Assist",
                        "Spring Boot + MCP + Gemini, the project answering this question right now",
                        "The same tools-and-guardrails pattern from Bank Assist, reused for a "
                                + "personal-site assistant that answers questions about this background "
                                + "and can size up fit against a pasted job description.",
                        "This is the service you're talking to. It's a small Spring Boot MCP server - "
                                + "the same shape as Bank Assist - with tools like getExperienceDetail and "
                                + "getProjectDetail instead of banking operations, running on Google's Gemini "
                                + "model this time (a free API key from Google AI Studio, no GCP billing "
                                + "account needed) rather than Claude, to show the same tool-calling agent "
                                + "pattern isn't tied to one model provider. The guardrail here is different: "
                                + "pasted job descriptions are untrusted text, so a PromptInjectionGuard "
                                + "screens them before they reach the model, and skill matching for the fit "
                                + "assessment is done in Java against a fixed skills list rather than left to "
                                + "the model to eyeball, so it can't claim a skill that isn't actually there.",
                        "https://github.com/naveekch/portfolio-assist#portfolio-assist",
                        List.of("Java", "Spring Boot", "Spring AI", "MCP", "Agentic AI", "Google Gemini")
                ),
                new ProjectEntry(
                        "AI Programming Assistant",
                        "React + TypeScript",
                        "A coding-assistant web app, built with React and TypeScript.",
                        "An AI programming assistant built with React and TypeScript, deployed on "
                                + "Netlify.",
                        "https://chilakhbhaiai.netlify.app/",
                        List.of("React", "TypeScript")
                ),
                new ProjectEntry(
                        "VisionTutor",
                        "Next.js + Daily.co + Claude 3.5 Sonnet",
                        "A real-time voice/vision AI tutoring app.",
                        "VisionTutor is a real-time voice and vision AI tutoring app built with "
                                + "Next.js, using Daily.co for the live video/audio pipeline and Claude 3.5 "
                                + "Sonnet as the tutoring model.",
                        "https://github.com/naveekch/vision-teacher-main?tab=readme-ov-file#visiontutor",
                        List.of("Next.js", "Claude", "Daily.co")
                )
        );
    }

    public static List<SkillGroup> skills() {
        return List.of(
                new SkillGroup("backend", List.of(
                        "Core Java", "J2EE", "Spring Boot", "Spring Core", "Spring IOC", "Spring MVC",
                        "Spring ORM", "Spring Batch", "Spring AI", "Hibernate", "JPA", "Spring Data JPA",
                        "JDBC", "JSP", "Servlets", "Microservices", "REST", "Web Services", "JMS",
                        "Kafka", "RabbitMQ", "gRPC", "Python", "Node.js", "SQL")),
                new SkillGroup("frontend", List.of(
                        "Angular (2+)", "React", "jQuery", "HTML5", "CSS3", "JavaScript", "TypeScript",
                        "RxJS", "Salesforce LWC", "Salesforce Apex")),
                new SkillGroup("apis", List.of(
                        "REST", "Web Services", "Swagger", "Postman", "REST Assured", "gRPC",
                        "GraphQL", "JWT", "OAuth2")),
                new SkillGroup("data", List.of(
                        "MySQL", "PostgreSQL", "Oracle", "DB2", "MongoDB", "DynamoDB", "Cassandra",
                        "Snowflake")),
                new SkillGroup("cloud-and-devops", List.of(
                        "AWS (Lambda, API Gateway, ECS, S3)", "Azure", "Docker", "Kubernetes",
                        "Terraform", "Maven", "Jenkins", "Git", "Bitbucket", "GitLab", "Azure Git",
                        "GitHub Actions", "Tomcat", "IntelliJ", "Eclipse", "JIRA")),
                new SkillGroup("testing", List.of(
                        "Playwright (TypeScript & Python)", "Selenium", "Cypress", "Jest", "Pytest",
                        "TestNG", "JUnit", "Cucumber/BDD", "Page Object Model", "data-driven testing",
                        "Allure/HTML reporting")),
                new SkillGroup("ai", List.of(
                        "Spring AI", "Model Context Protocol (MCP)", "Agentic AI / tool-calling agents",
                        "LLM APIs (Claude, OpenAI)", "Google Gemini", "GitHub Copilot",
                        "multi-model routing with provider fallback", "prompt-driven integrations"))
        );
    }

    public static List<EducationEntry> education() {
        return List.of(
                new EducationEntry("Master's, Computer Science", "University of North Texas", ""),
                new EducationEntry("Bachelor's, Computer Science", "VNRVJIET, Hyderabad", "")
        );
    }

    public static List<CertificationEntry> certifications() {
        return List.of(
                new CertificationEntry(
                        "AWS Certified Developer – Associate", "Amazon Web Services",
                        "Valid until Dec 2027",
                        "https://www.credly.com/badges/8c7e50ec-8ae7-4b15-9fdf-6c8c6d3b7ad7/linked_in_profile"),
                new CertificationEntry(
                        "Apache Cassandra Developer Certification", "DataStax", "",
                        "https://certification.mettl.com/datastax/applicant/verify-certification")
        );
    }
}
