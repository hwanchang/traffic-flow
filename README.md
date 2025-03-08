# 트래픽 제어 구조 설계

제한적인 상황으로 실제 운영환경에서 확인하기 어려운 문제들을 확인해보고 구조를 적용하여 실험합니다.

다양한 상황에 대한 여러 트래픽 제어 구조들 계속해서 적용하여 문제를 해결하는 노하우를 익히는게 목적입니다.

## 사용한 기술

- Spring Boot
- Kotlin
- Spring Data JPA
- Spring Data Redis
- Resilience4j
- Kafka

## 현재 적용된 구조

- [x] client 요청 시 메인 로직 처리 후 이벤트 발행으로 비동기 처리
- [x] 이벤트 컨슈머 측에서 컨슘 시 RateLimiter 적용
    - 외부 이슈로 인해 요청에 제한이 필요한 경우 제한된 tps만 처리 가능하도록 설정
- [x] 이벤트 처리 실패 시 kafka DLT 활용하여 event failover 처리
- [x] CircuitBreaker 적용하여 외부요청에 대한 실패가 많아질 경우 요청 자동 차단
    - 차단된 요청은 모두 외부 이슈 해결 이후 event failover 처리하도록 별도로 저장

## 앞으로 적용할 내용

- [ ] Spring Data JPA 사용으로 인해 코루틴 활용 비동기 처리 시 blocking 이슈
    - Spring Data R2DBC 적용하여 non-blocking 으로 DB작업 하도록 적용
