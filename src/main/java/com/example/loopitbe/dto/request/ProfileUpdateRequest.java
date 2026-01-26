package com.example.loopitbe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record ProfileUpdateRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @Past(message = "생년월일은 과거 날짜여야 합니다.")
        LocalDate birthdate
) {
}