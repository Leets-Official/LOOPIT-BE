package com.example.loopitbe.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class KakaoUserCreateRequest {
    @NotBlank
    private String kakaoId;
    @NotBlank
    @Size(max = 10)
    private String nickname;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    private LocalDate birthdate;
    private String profileImage;

    public String getKakaoId() {
        return kakaoId;
    }
    public String getEmail() {return email;}
    public String getNickname() {
        return nickname;
    }
    public String getName() { return name; }
    public LocalDate getBirthdate() { return birthdate; }
    public String getProfileImage() { return profileImage; }
}
