package com.socialLogin.socialLogin.service;


import com.socialLogin.socialLogin.dto.request.auth.CheckCertificationRequestDto;
import com.socialLogin.socialLogin.dto.request.auth.EmailCertificationRequestDto;
import com.socialLogin.socialLogin.dto.request.auth.IdCheckRequestDto;
import com.socialLogin.socialLogin.dto.response.auth.CheckCertificationResponseDto;
import com.socialLogin.socialLogin.dto.response.auth.EmailCertificationResponseDto;
import com.socialLogin.socialLogin.dto.response.auth.IdCheckResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
}
