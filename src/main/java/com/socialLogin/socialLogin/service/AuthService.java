package com.socialLogin.socialLogin.service;


import com.socialLogin.socialLogin.dto.request.auth.IdCheckRequestDto;
import com.socialLogin.socialLogin.dto.response.auth.IdCheckResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
}
