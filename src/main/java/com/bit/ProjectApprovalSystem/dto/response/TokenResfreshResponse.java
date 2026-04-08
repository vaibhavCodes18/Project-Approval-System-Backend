package com.bit.ProjectApprovalSystem.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResfreshResponse {
    private String accessToken;

    @JsonIgnore
    private String refreshToken;
}
