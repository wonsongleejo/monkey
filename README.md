<h1 align="center"> 🐵 monkey 팝업 스토어 🙊 </h1>

<div  align="center">
  <img width="77%" src="/docs/images/banner.png" alt="LOGO">
</div>

---

## 🐵 프로젝트 목표 & 소개
> - 본 프로젝트는 팝업스토어 예약 및 한정 상품 예약 기능을 제공하는 플랫폼을 개발하는 것을 목표로 한다.
> - 사용자는 다양한 팝업스토어를 확인하고 예약할 수 있으며, 스토어 예약 완료 후 한정 상품도 별도로 예약할 수 있다.
> - 운영자는 팝업스토어 등록과 예약 관리를 효율적으로 수행할 수 있다.
#### 🍌 사용자에게 다양한 팝업스토어 정보 제공, 간편한 스토어 예약 기능 지원
#### 🍌팝업스토어 예약자에 한해 한정 상품 예약 기회 제공
#### 🍌스토어 운영자가 팝업스토어 등록 및 예약 관리를 쉽게 할 수 있도록 운영 편의성 제공


- - -

## 🐵 팀원 소개 및 담당역할
| **역할**                    |     **담당자**     | **세부 업무**                                                                                                                                                                                                                      |
|---------------------------|:---------------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **상품,<br> 상품 예약 API**     |   **원**지윤<br>   | - 상품, 상품예약 CRUD 구현<br> - 공통 모듈에 예외/응답, JPA Auditing, AuditorAware 구현 및 JitPack 배포 <br> - Redis 캐싱을 통해 상품 조회 성능 최적화 <br> - 상품 예약 시 분산 락을 적용하여 재고 동시성 문제 해결 <br> - 예약 취소 시 재고 복구를 위한 보상 트랜잭션 적용 <br> |
| **스토어 예약,<br> 슬랙 알림 API** | **송**예지<br>(팀장) | - 스토어 예약 CRUD 구현<br> - Redis 캐싱 및 Redisson 락을 활용한 동시성 제어 <br> - JMeter 테스트 기반으로 최적의 락 방식 적용 <br> - 예약 기능별 서비스 버전 분리 및 관리 <br> - Slack 포맷터 기반 메시지 템플릿 구성 및 Slack 알림 전송                                            |
| **스토어 API**               |     **이**태훈     | - 스토어, 스토어 시간대 CRU 구현 <br> - 스토어 전체 조회 시 캐싱하여 조회                                                                                                                                                         |
| **인증인가,<br> 사용자 API**     |     **조**수빈     | - 회원가입, 로그인, 회원정보 수정 및 예약 내역 조회 기능 구현<br> - JWT 기반 인증 및 토큰 발급, 인증 필터로 사용자 요청 검증<br> - AOP와 @CheckUserRole을 통한 역할 기반 접근 제어 구현<br> - Redis 캐싱으로 사용자별 예약 내역 조회 성능 최적화<br> - 재고 감소 시 락을 활용한 동시성 제어 처리                              |


- - -

## 🐵 프로젝트 설계
<details>
  <summary> ERD </summary>
  <img alt="erd" src="/docs/images/erd.png" width="800">
</details><br>
<details>
  <summary> 인프라 설계 </summary>
  <img alt="infra" src="/docs/images/infra.png" width="700">
</details><br>
<details>
  <summary> 도메인 주도 설계 </summary>
  <img alt="ddd" src="/docs/images/ddd.png" width="700">
</details><br>



- - -

## 🐵 주요 기능

### 1. MSA 아키텍처 설계 및 구축
- 멀티모듈 기반 마이크로서비스 아키텍처 설계
- 각 도메인은 독립적인 Spring Boot 애플리케이션으로 구성
- 서비스 간 통신은 FeignClient를 활용한 RESTful API 기반 인터페이스로 처리
- Spring Cloud Eureka를 이용한 서비스 디스커버리 및 Spring Cloud Gateway를 통한 API 라우팅

### 2. 캐싱 도입을 통한 조회 응답 속도 개선
- 반복되는 요청을 줄이기 위해 Redis 캐시를 적용하여 응답 속도 개선
- 캐싱 적용 전후 JMeter 기반 성능 측정을 통해 응답 속도 및 처리량 개선 확인

### 3. 비동기 이벤트 기반 시스템 구성
- Kafka를 통한 예약 프로세스 비동기 연동
- Producer-Consumer 구조로 서비스 간 결합도를 낮추고 확장성을 높임

### 4. 데이터 정합성 및 동시성 제어
- Redis 기반 분산 락을 도입하여 스토어/상품 예약 및 상품 재고 차감 시 동시 접근 제어
- 상품 예약에 비관적 락을 적용하여 데이터 정합성 문제 해결

### 5. Docker 기반 통합 개발 환경 구성
- `docker-compose`로 개발 환경 구축: 모든 마이크로서비스, Kafka, Redis, PostgreSQL 등을 한 번에 실행
- 서비스 간 의존성을 최소화한 실행 및 테스트 환경 구성

### 6. 보안 및 인증 체계 구현
- JWT 기반 사용자 인증 및 권한 검증 로직 구현
- 사용자 요청 시 토큰에서 사용자 정보를 파싱하여 권한 기반 접근 제어 적용

### 7. API 명세 및 테스트 자동화
- Swagger 기반의 API 문서 자동 생성
- FeignClient 모킹을 통해 테스트 구현


- - -

## 🐵 기술 스택
- **Language** <div> <img src="https://img.shields.io/badge/java17-green"/></div>

- **Backend Framework** <div> <img src = "https://img.shields.io/badge/Spring_Boot-6DB33F?&logo=spring-boot&logoColor=white"> <img src = "https://img.shields.io/badge/Spring_Data_JPA-6DB33F?&logo=spring&logoColor=white"> <img src = "https://img.shields.io/badge/Spring_Cloud_Gateway-6DB33F?&logo=spring&logoColor=white"> <img src = "https://img.shields.io/badge/Eureka-6DB33F?&logo=spring&logoColor=white"> <img src = "https://img.shields.io/badge/OpenFeign-6DB33F?&logo=spring&logoColor=white"> </div>

- **Database** <div> <img src = "https://img.shields.io/badge/PostgreSQL-005571?&logo=postgresql&logoColor=white"> <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&logo=redis&logoColor=white"> </div> 

- **Messaging** <div> <img src = "https://img.shields.io/badge/Apache_Kafka-231F20?&logo=apache-kafka&logoColor=white"></div>

- **Auth** <div> <img src = "https://img.shields.io/badge/Spring_Security-6DB33F?&logo=spring-security&logoColor=white"> <img src = "https://img.shields.io/badge/JWT-6DB33F?&logo=jsonwebtokens&logoColor=white"></div>

- **Infra** <div> <img src = "https://img.shields.io/badge/Docker-2CA5E0?&logo=docker&logoColor=white"> </div>

- **IDE** <div> <img src = "https://img.shields.io/badge/IntelliJ_IDEA-807d7d.svg?&logo=intellij-idea&logoColor=white"> </div>


- - -

## 🐵 기술적 의사결정
#### 🍌 [DDD 및 MSA 도입](https://github.com/wonsongleejo/monkey/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-DDD-%EB%B0%8F-MSA-%EB%8F%84%EC%9E%85)
#### 🍌 [공통모듈 분리하여 JitPack에 배포](https://github.com/wonsongleejo/monkey/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-%EA%B3%B5%ED%86%B5%EB%AA%A8%EB%93%88-%EB%B6%84%EB%A6%AC%ED%95%98%EC%97%AC-JitPack%EC%97%90-%EB%B0%B0%ED%8F%AC)
#### 🍌 [Redis 적용](https://github.com/wonsongleejo/monkey/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-Redis-%EC%A0%81%EC%9A%A9)
#### 🍌 [서비스 버전 분리](https://github.com/wonsongleejo/monkey/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%B2%84%EC%A0%84-%EB%B6%84%EB%A6%AC)
#### 🍌 [Kafka 도입](https://github.com/wonsongleejo/monkey/wiki/%5B%EA%B8%B0%EC%88%A0%EC%A0%81-%EC%9D%98%EC%82%AC%EA%B2%B0%EC%A0%95%5D-Kafka-%EB%8F%84%EC%9E%85)

- - -

## 🐵 트러블슈팅
#### 🍌 [상품 예약 시스템 – DIP 위반 문제 해결](https://github.com/wonsongleejo/monkey/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85%5D-%EC%83%81%ED%92%88-%EC%98%88%EC%95%BD-%EC%8B%9C%EC%8A%A4%ED%85%9C-%E2%80%93-DIP-%EC%9C%84%EB%B0%98-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)
#### 🍌 [한정 상품 재고 Redis 적용 후 데이터 정합성 문제 해결](https://github.com/wonsongleejo/monkey/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85%5D-%ED%95%9C%EC%A0%95-%EC%83%81%ED%92%88-%EC%9E%AC%EA%B3%A0-Redis-%EC%A0%81%EC%9A%A9-%ED%9B%84-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%95%ED%95%A9%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0)
#### 🍌 [FeignClient로 유저 정보 조회 중 인증 헤더 누락 발생](https://github.com/wonsongleejo/monkey/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85%5D-FeignClient%EB%A1%9C-%EC%9C%A0%EC%A0%80-%EC%A0%95%EB%B3%B4-%EC%A1%B0%ED%9A%8C-%EC%A4%91-%EC%9D%B8%EC%A6%9D-%ED%97%A4%EB%8D%94-%EB%88%84%EB%9D%BD-%EB%B0%9C%EC%83%9D)
#### 🍌 [상품 예약에 Redisson 기반 분산 락 적용](https://github.com/wonsongleejo/monkey/wiki/%5B%ED%8A%B8%EB%9F%AC%EB%B8%94-%EC%8A%88%ED%8C%85%5D-%EC%83%81%ED%92%88-%EC%98%88%EC%95%BD%EC%97%90-Redisson-%EA%B8%B0%EB%B0%98-%EB%B6%84%EC%82%B0-%EB%9D%BD-%EC%A0%81%EC%9A%A9)


- - -

## 🐵 협업
### 🍌 GIT Branch
- `main` : 서비스 운영 브랜치
- `dev` : 개발 환경 브랜치 - 개별적으로 작업했던 내용을 검토 후 dev에 merge
- `feat/(서비스명)/...` : 서비스별 세부 브랜치
- `fix/(서비스명)...` : 서비스별 수정되는 작업들을 위한 브랜치

### 🍌 Commit & Pull-Request Message
|    Division    | Message                 |
|:--------------:|:------------------------|
|   "feat: ~ "   | 새로운 기능 추가               |
|   "fix: ~ "    | 버그 수정                   |
| "refactor: ~ " | 기능이나 성능 개선 및 수정         |
|  "style: ~ "   | 주석 제거, 코드 줄바꿈 등의 사소한 변경 |
|  "delete: ~ "  | 파일을 삭제만 한 경우            |

### 🍌 GitHub 관리 및 소통
- **Pull Request를 통한 코드 리뷰**
    - dev에 pr을 올린 후 두 명 이상의 승인을 거쳐야 merge 할 수 있음
    - 코드 변경 사항에 대해 팀원들과 공유하고 검토
- **GitHub Issues를 통한 이슈 관리**
    - 이슈 트래킹을 통해 진행사항을 효율적으로 파악
- **데일리 스크럼을 통한 커뮤니케이션**
    - 매일 스크럼을 진행하여 모든 팀원의 진행 상황 공유


<br>