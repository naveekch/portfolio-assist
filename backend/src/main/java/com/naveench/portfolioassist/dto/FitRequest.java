package com.naveench.portfolioassist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FitRequest(
        @NotBlank @Size(max = 8000) String jobDescription
) {
}
