package com.socialLogin.socialLogin.service.iplement;

import com.socialLogin.socialLogin.common.CertificationNumber;
import com.socialLogin.socialLogin.dto.request.auth.EmailCertificationRequestDto;
import com.socialLogin.socialLogin.dto.request.auth.IdCheckRequestDto;
import com.socialLogin.socialLogin.dto.response.ResponseDto;
import com.socialLogin.socialLogin.dto.response.auth.EmailCertificationResponseDto;
import com.socialLogin.socialLogin.dto.response.auth.IdCheckResponseDto;
import com.socialLogin.socialLogin.entity.CertificationEntity;
import com.socialLogin.socialLogin.provider.EmailProvider;
import com.socialLogin.socialLogin.repository.CertificationRepository;
import com.socialLogin.socialLogin.repository.UserRepository;
import com.socialLogin.socialLogin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private final CertificationRepository certificationRepository;

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

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();

            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) {
                return EmailCertificationResponseDto.duplicateId();
            }

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccessed) {
                return EmailCertificationResponseDto.mailSendFail();
            }

            CertificationEntity certificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }
}
