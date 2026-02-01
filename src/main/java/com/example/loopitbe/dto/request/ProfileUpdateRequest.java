package com.example.loopitbe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public class ProfileUpdateRequest {

        @NotBlank(message = "닉네임은 필수입니다.")
        private String nickname;

        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @Past(message = "생년월일은 과거 날짜여야 합니다.")
        private LocalDate birthdate;

        public ProfileUpdateRequest() {
        }

        public ProfileUpdateRequest(String nickname, String name, String email, LocalDate birthdate) {
                this.nickname = nickname;
                this.name = name;
                this.email = email;
                this.birthdate = birthdate;
        }

        public String getNickname() {
                return nickname;
        }

        public void setNickname(String nickname) {
                this.nickname = nickname;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public LocalDate getBirthdate() {
                return birthdate;
        }

        public void setBirthdate(LocalDate birthdate) {
                this.birthdate = birthdate;
        }
}