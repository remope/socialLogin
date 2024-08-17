package com.socialLogin.socialLogin.controller;

import com.socialLogin.socialLogin.dto.request.auth.IdCheckRequestDto;
import com.socialLogin.socialLogin.dto.response.auth.IdCheckResponseDto;
import com.socialLogin.socialLogin.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck (
            @RequestBody @Valid IdCheckRequestDto requestBody
            ) {
        ResponseEntity<? super IdCheckResponseDto> response = authService.idCheck(requestBody);
        return response;
    }
}
