# 📱 LOOPIT-BE

> **IT 수리점 찾기 및 중고판매 서비스 플랫폼, LOOPIT의 백엔드 서버 저장소입니다. (2025.12.22 ~ 2026.2.13)**

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)<br>
![Leets6기_최종발표_(Loopit)-이미지-0](https://github.com/user-attachments/assets/314e4e6f-cf67-4337-8065-5ccc6d8c9cff)

---

## 👥 Team Members (Back-end)

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/gichanGim">
        <img src="https://github.com/gichanGim.png" width="100px;" alt=""/>
      </a>
      <br />
    </td>
    <td align="center">
      <a href="https://github.com/sxvxnxwt">
        <img src="https://github.com/sxvxnxwt.png" width="100px;" alt=""/>
      </a>
      <br />
    </td>
    <td align="center">
      <a href="https://github.com/soyun-git121">
        <img src="https://github.com/soyun-git121.png" width="100px;" alt=""/>
      </a>
      <br />
    </td>
  </tr>
    <td align="center">
      <a href="https://github.com/gichanGim" title="Code">김기찬(파트장)</a>
    </td>
    <td align="center">
      <a href="https://github.com/sxvxnxwt" title="Code">김민지</a>
    </td>
    <td align="center">
      <a href="https://github.com/soyun-git121" title="Code">박소윤</a>
    </td>
  <tr>
    
  </tr>
</table>

---

## 📖 Project Overview
### **LOOPIT**은 중고 IT 기기 판매, 근처 IT기기 수리점 탐색 및 챗봇을 통해 수리비 견적 솔루션을 제공하는 플랫폼입니다.<br>
이 리포지토리는 LOOPIT의 핵심 비즈니스 로직과 데이터 처리를 담당하는 **RESTful API 서버**입니다.<br>

### ✨ Key Features
* 사용자 인증/인가 (JWT, Kakao OAuth)
* 중고 IT 기기 매물 등록, 검색, 필터링 및 CRUD
* Kakao Maps API 기반 근처 IT기기 수리점 탐색
* WebSocket(STOMP), Redis pub/sub 로직 기반 구매자-판매자 1:1 실시간 채팅
* Google Gemini API를 활용한 맞춤형 챗봇 서비스 (공식/사설 수리점 비용 비교하여 수리비 견적 산출)
* 거래 단계별 상태 관리 (판매중 → 예약중 → 거래완료)

---

## 🏗️ System Architecture

![Leets6기_최종발표_(Loopit)-이미지-17](https://github.com/user-attachments/assets/fcb32291-68cf-4250-a9f7-70adae6cdb4f)


---
