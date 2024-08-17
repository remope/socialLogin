package com.socialLogin.socialLogin.service.iplement;

import com.socialLogin.socialLogin.dto.request.auth.IdCheckRequestDto;
import com.socialLogin.socialLogin.dto.response.ResponseDto;
import com.socialLogin.socialLogin.dto.response.auth.IdCheckResponseDto;
import com.socialLogin.socialLogin.repository.UserRepository;
import com.socialLogin.socialLogin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) {
                return IdCheckResponseDto.duplicateID();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return IdCheckResponseDto.success();
    }
}
