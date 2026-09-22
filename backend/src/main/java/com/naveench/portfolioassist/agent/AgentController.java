package com.naveench.portfolioassist.agent;

import com.naveench.portfolioassist.dto.AskRequest;
import com.naveench.portfolioassist.dto.AskResponse;
import com.naveench.portfolioassist.dto.FitRequest;
import com.naveench.portfolioassist.dto.GroundingDemoResponse;
import com.naveench.portfolioassist.dto.UngroundedResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@Valid @RequestBody AskRequest request) {
        return agentService.ask(request.message());
    }

    @PostMapping("/assess-fit")
    public AskResponse assessFit(@Valid @RequestBody FitRequest request) {
        return agentService.assessFit(request.jobDescription());
    }

    /** Same question answered with tools and without, for the grounding demo. */
    @PostMapping("/grounding-demo")
    public GroundingDemoResponse groundingDemo(@Valid @RequestBody AskRequest request) {
        return agentService.compareGrounding(request.message());
    }

    /** Just the ungrounded half, for revealing it under an answer already on screen. */
    @PostMapping("/ungrounded")
    public UngroundedResponse ungrounded(@Valid @RequestBody AskRequest request) {
        return agentService.askUngrounded(request.message());
    }
}
