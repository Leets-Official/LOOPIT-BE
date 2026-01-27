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

        // 1. 기본 생성자 (JSON 파싱 및 데이터 바인딩 시 필수)
        public ProfileUpdateRequest() {
        }

        // 2. 전체 필드 생성자
        public ProfileUpdateRequest(String nickname, String name, String email, LocalDate birthdate) {
                this.nickname = nickname;
                this.name = name;
                this.email = email;
                this.birthdate = birthdate;
        }

        // 3. Getter 및 Setter 메서드
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