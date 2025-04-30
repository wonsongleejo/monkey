# 🐵 monkey 팝업 스토어 🙊

![배너](/docs/images/banner.png)

- - -

## 프로젝트 소개
> - 본 프로젝트는 팝업스토어 예약 및 한정 상품 예약 기능을 제공하는 플랫폼을 개발하는 것을 목표로 한다.
> - 사용자는 다양한 팝업스토어를 확인하고 예약할 수 있으며, 스토어 예약 완료 후 한정 상품도 별도로 예약할 수 있다.
> - 운영자는 팝업스토어 등록과 예약 관리를 효율적으로 수행할 수 있다.

- - -

## 프로젝트 목표
#### 🐵 사용자에게 다양한 팝업스토어 정보 제공, 간편한 스토어 예약 기능 지원<br>
#### 🙈 팝업스토어 예약자에 한해 한정 상품 예약 기회 제공<br>
#### 🐵 스토어 운영자가 팝업스토어 등록 및 예약 관리를 쉽게 할 수 있도록 운영 편의성 제공<br>

- - -

## 인프라 설계도
![인프라](/docs/images/infra.png)

- - -

## 적용 기술

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
- `docker-compose`기반 개발 환경 구축: 모든 마이크로서비스, Kafka, Redis, PostgreSQL 등을 한 번에 실행 가능
- 서비스 간 의존성을 최소화한 독립 실행 및 테스트 환경 구성

### 6. 보안 및 인증 체계 구현
- JWT 기반 사용자 인증 및 권한 검증 로직 구현
- 사용자 요청 시 토큰에서 사용자 정보를 파싱하여 접근 제어 적용

### 7. API 명세 및 테스트 자동화
- Swagger 기반의 API 문서 자동 생성
- FeignClient 모킹을 통해 테스트 구현


- - -

# 기술스택
- **Language** <div> <img src="https://img.shields.io/badge/java17-pink"/>
 </div>

- **Backend Framework** <div> <img src = "https://img.shields.io/badge/Spring_Boot-6DB33F?&logo=spring-boot&logoColor=white"> <img src = "https://img.shields.io/badge/Spring_Data_JPA-6DB33F?&logo=spring&logoColor=white"> <img src = "https://img.shields.io/badge/Spring_Cloud_Gateway-6DB33F?&logo=spring&logoColor=white"> <img src = "https://img.shields.io/badge/Eureka-6DB33F?&logo=spring&logoColor=white"> <img src = "https://img.shields.io/badge/OpenFeign-6DB33F?&logo=spring&logoColor=white"> </div>

- **Database** <div> <img src = "https://img.shields.io/badge/PostgreSQL-005571?&logo=postgresql&logoColor=white"> <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&logo=redis&logoColor=white"> </div> 

- **Messaging** <div> <img src = "https://img.shields.io/badge/Apache_Kafka-231F20?&logo=apache-kafka&logoColor=white"></div>

- **Auth** <div> <img src = "https://img.shields.io/badge/Spring_Security-6DB33F?&logo=spring-security&logoColor=white"> <img src = "https://img.shields.io/badge/JWT-6DB33F?&logo=jsonwebtokens&logoColor=white"></div>

- **Infra** <div> <img src = "https://img.shields.io/badge/Docker-2CA5E0?&logo=docker&logoColor=white"> </div>

- **IDE** <div> <img src = "https://img.shields.io/badge/IntelliJ_IDEA-807d7d.svg?&logo=intellij-idea&logoColor=white"> </div>


- - -

## 기술적 의사결정
#### 

- - -

## 트러블슈팅
#### 

- - -

# 팀원 소개 및 담당역할
| **역할**                    |   **담당자**   | **세부 업무**                                                                                                                                                                                                                     |
|---------------------------|:-----------:|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **상품,<br> 상품 예약 API**     |   원지윤<br>   | - 상품/상품예약 CRUD 구현<br> - 공통 모듈을 통해 전역 예외 처리/응답, BaseEntity, JPA Auditing 및 AuditorAware 구현을 통합하여 JitPack으로 배포 <br> - Redis 캐싱을 통해 상품 조회 성능 최적화 <br> - 상품 예약 시 분산 락을 적용하여 재고 동시성 문제 해결 <br> - 예약 취소 시 재고 복구를 위한 보상 트랜잭션 적용 <br> |
| **스토어 예약,<br> 슬랙 알림 API** | 송예지<br>(팀장) | - 스토어 예약 CRUD 구현, 슬랙 알림 서비스 연동<br> - Redis 캐싱 및 Redisson 락을 활용한 동시성 제어 <br> - JMeter 테스트 기반으로 최적의 락 방식 적용 <br> - 예약 기능별 서비스 버전 분리 및 관리 <br> - Slack 포맷터 기반 메시지 템플릿 구성 및 Slack 알림 전송                                           |
| **스토어 API**               |     이태훈     | - 스토어 CRUD 구현<br> - <br>- <br> - <br> - <br> - <br> - <br> - <br> - <br> -                                                                                                                                                    |
| **인증인가,<br> 사용자 API**     |     조수빈     | - 회원가입,로그인 등의 사용자 정보CRUD 구현<br> - <br> - <br> - <br> - <br> -                                                                                                                                                                 |
