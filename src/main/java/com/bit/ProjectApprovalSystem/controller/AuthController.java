package com.bit.ProjectApprovalSystem.controller;

import com.bit.ProjectApprovalSystem.dto.request.CreateUserRequest;
import com.bit.ProjectApprovalSystem.dto.request.LoginRequest;
import com.bit.ProjectApprovalSystem.dto.request.RegisterRequest;
import com.bit.ProjectApprovalSystem.dto.response.AuthResponse;
import com.bit.ProjectApprovalSystem.dto.response.TokenResfreshResponse;
import com.bit.ProjectApprovalSystem.dto.response.UserResponse;
import com.bit.ProjectApprovalSystem.response.ApiResponse;
import com.bit.ProjectApprovalSystem.service.interfaces.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days in seconds

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site:Lax}")
    private String cookieSameSite;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> addStudent(@RequestBody RegisterRequest registerRequest) {
        UserResponse response = authService.saveStudent(registerRequest);
        ApiResponse<?> apiResponse = new ApiResponse<>(201, "A student was successfully created!", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/hod/register")
    public ResponseEntity<?> saveHodGuide(@RequestBody CreateUserRequest createUserRequest) {
        UserResponse response = authService.saveHod(createUserRequest);
        ApiResponse<?> apiResponse = new ApiResponse<>(201, "A hod was successfully created!", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(loginRequest);

        addRefreshCookie(response, authResponse.getRefreshToken(), REFRESH_COOKIE_MAX_AGE);

        ApiResponse<?> apiResponse = new ApiResponse<>(200, "User successfully logged in!", authResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> userProfile() {
        UserResponse userResponse = authService.userProfile();
        ApiResponse<?> apiResponse = new ApiResponse<>(200, "User successfully fetched!", userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            authService.logout(refreshToken);
        }

        addRefreshCookie(response, "", 0);

        ApiResponse<?> apiResponse = new ApiResponse<>(200, "User successfully logged out!", null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> tokenRefresh(
            @CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Refresh Token is missing in cookies", null));
        }

        TokenResfreshResponse responseDto = authService.refreshToken(refreshToken);

        // Rotate the refresh token cookie
        addRefreshCookie(response, responseDto.getRefreshToken(), REFRESH_COOKIE_MAX_AGE);

        ApiResponse<?> apiResponse = new ApiResponse<>(200, "Token refreshed successfully", responseDto);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    private void addRefreshCookie(HttpServletResponse response, String tokenValue, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, tokenValue)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
