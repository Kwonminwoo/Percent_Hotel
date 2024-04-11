# Percent Hotel
**숙소 양도/거래 플랫폼 "퍼센트 호텔"**

- 일시 - V1: 2023.12.06 ~ 2024.01.30 <br>
&emsp;&emsp; - V2: 2024.03 ~ <br>
- 구성원: PM 5명 / UX/UI 1명 / Frontend 5명 / Backend 4명
 

## 👷‍♂️ 권민우 구현 내용
- 기획
  - ERD 설계
- 결제 시스템
  - 카카오 API를 연동한 결제 로직 구현
  - 동시성 문제를 해결하기 위해 여러가지 기술을 적용 및 성능 테스트
  - 
- 인증/인가 구현
  - Spring Security를 이용
  - JWT를 이용해 로그인 유지
  - Redis를 이용한 refresh 토큰 활용
- 네이버 지도 API 사용
- 동적 웹 크롤링

<br>

## 🪛 기능
- 약속 조회
  - 참가하고 있는 약속 전체 조회
  - 하나의 약속 조회
- 약속 생성
- 약속 참여
  - 참여 코드를 통해 약속에 참여
- 투표
  - 참여 중인 약속의 일정을 투표
  - 약속 장소를 지도로 투표
- 맛집 조회
  - 투표 완료 된 약속의 약속 장소의 맛집 추천

<br>

## 🛠️ 기술 스택


## ⌨️ 설정 
- 자바 버전: 17

- 스프링 버전: 6.0

- 스프링 부트 버전: 3.2.1


## ERD
![(정리용) 숙취방지 최종 ERD - v1 (1)](https://github.com/SCBJ-7/SCBJ-BE/assets/63856521/8a6a60f0-333e-4569-b217-0b9c514d7389)


## ARCHITECTURE
![숙취방지 최종 ARCHITECTURE](https://github.com/SCBJ-7/SCBJ-BE/assets/63856521/74661494-beb8-4ae7-a7d5-d864b4ac64dc)

## 결제도메인 with 동시성제어
- 카카오페이 로직

  ![숙취방지-결제도메인 drawio](https://github.com/SCBJ-7/SCBJ-BE/assets/63856521/e4cce2d3-3c1d-42d6-9f51-caa34b0a6e06)
- Optimisitc Lock을 활용한 동시성 제어 : https://github.com/SCBJ-7/SCBJ-BE/discussions/309
- Pessimistic Lock을 활용한 동시성 제어 : https://github.com/SCBJ-7/SCBJ-BE/discussions/310
- Lettuce(Redis)를 활용한 동시성 제어 : https://github.com/SCBJ-7/SCBJ-BE/discussions/312
- Redisson(Redis)을 활용한 동시성 제어 : https://github.com/SCBJ-7/SCBJ-BE/discussions/311
