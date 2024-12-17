# 📦 재입고 알림 시스템

## 프로젝트 소개

**재입고 알림 시스템**은 재고가 발생한 상품에 대해 알림을 설정한 사용자들에게 알림 메시지를 전송하는 시스템입니다.

관리자는 상품 재입고 알림을 수동으로 재전송할 수 있으며, 실패 시 마지막 성공한 사용자 이후부터 알림을 재전송합니다.

---

## 기술 스펙

| 구분 | 사용 기술 |
| --- | --- |
| **언어** | Java 17 |
| **프레임워크** | Spring Boot 3.3.6 |
| **빌드 툴** | Gradle |
| **DB** | MySQL |
| **ORM** | Spring Data JPA |
| **테스트** | JUnit 5, Mockito |
| **배포** | Docker |

---

## 📑 API 명세서

### 1. **재입고 알림 전송 API**

- **요청 경로**: `POST /products/{productId}/notifications/re-stock`
- **설명**: 재고가 있는 상품에 대해 알림 메시지를 전송합니다.

| 파라미터 | 타입 | 설명 | 예시 |
| --- | --- | --- | --- |
| productId | Path | 상품 ID | 1 |

**응답 예시**

```json
{
  "status": 200
}

```

### 2. **재입고 알림 재전송 API (수동)**

- **요청 경로**: `POST /admin/products/{productId}/notifications/re-stock`
- **설명**: 마지막 성공한 사용자 이후부터 알림 메시지를 다시 전송합니다.

| 파라미터 | 타입 | 설명 | 예시 |
| --- | --- | --- | --- |
| productId | Path | 상품 ID | 1 |

**응답 예시**

```json
{
  "status": 200
}

```

---

## 🗄️ 테이블 정보

### **1. Product (상품 테이블)**

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| product_id | BIGINT | 상품 ID (PK) |
| restock_round | INT | 재입고 알림 회차 |
| stock_status | ENUM | 재고 상태(IN_STOCK, OUT_OF_STOCK) |

### **2. ProductNotificationHistory (알림 기록 테이블)**

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | BIGINT | 기록 ID (PK) |
| product_id | BIGINT | 상품 ID (FK) |
| restock_round | INT | 재입고 알림 회차 |
| notification_status | ENUM | 알림 상태(IN_PROGRESS, COMPLETED 등) |
| last_sent_user_id | BIGINT | 마지막 알림 성공 사용자 ID |

### **3. ProductUserNotification (알림 설정 사용자 테이블)**

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | BIGINT | 설정 ID (PK) |
| product_id | BIGINT | 상품 ID (FK) |
| user_id | BIGINT | 사용자 ID |
| is_active | BOOLEAN | 알림 활성화 여부 |

### **4. ProductUserNotificationHistory (사용자 알림 히스토리)**

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | BIGINT | 히스토리 ID (PK) |
| product_id | BIGINT | 상품 ID (FK) |
| user_id | BIGINT | 사용자 ID |
| restock_round | INT | 재입고 알림 회차 |
| sent_at | DATETIME | 알림 전송 시간 |

---

## 🛠️ 개발 구조

### **패키지 구조**

```
realarmproduct
├── common
│   ├── constant             // 상수 관리
│   ├── enums                // 열거형 타입
│   ├── exception            // 예외 처리
│   ├── handler              // 예외 핸들러
│   ├── BaseTimeEntity       // 공통 엔티티 시간 관리
│   └── ErrorResponse        // 에러 응답 객체
│
├── productNotification
│   ├── controller
│   │   └── NotificationController   // API 컨트롤러
│   │
│   ├── domain
│   │   ├── Product                     // 상품 도메인
│   │   ├── ProductNotificationHistory  // 알림 기록 도메인
│   │   ├── ProductUserNotification     // 사용자 알림 설정 도메인
│   │   └── ProductUserNotificationHistory // 사용자 알림 히스토리
│   │
│   ├── repository
│   │   ├── entity
│   │   │   ├── ProductRepository
│   │   │   ├── ProductNotificationHistoryRepository
│   │   │   ├── ProductUserNotificationHistoryRepository
│   │   │   └── ProductUserNotificationRepository
│   │   └── ReStockNotificationRepositoryImpl  // 커스텀 리포지토리 구현체
│   │
│   └── service
│       ├── AlarmSender                 // 알림 전송 인터페이스
│       ├── MockAlarmSender             // Mock 알림 전송 구현체
│       └── ReStockNotificationService  // 재입고 알림 서비스 로직
│
└── RealarmProductApplication          // 메인 애플리케이션 클래스


```

### **기능 구조**

- **Controller**: API 요청을 받아 Service 레이어로 전달합니다.
- **Service**: 재입고 알림 및 실패 알림 재전송 기능 구현 및 알림 전송 인터페이스 및 Mock 구현체 포함
- **Repository**: JPA 기반 데이터 접근 로직 수행.
- **Domain**: 상태 변경과 비즈니스 로직을 캡슐화.
- **Support**: 알림 전송 인터페이스 및 Mock 구현체 포함.

---

## ⚙️ 실행 방법

1. **빌드**
    
    ```bash
    ./gradlew clean build
    
    ```
    
2. **애플리케이션 실행**
    
    ```bash
    docker-compose up
    
    ``` 

---

## 📋 테스트

** 소형테스트(단위테스트) 진행 **

- **컨트롤러 테스트**: MockMvc를 사용해 컨트롤러 API 요청 검증 및 Request 파라미터 Validation 검증
- **비지니스 테스트**: JUnit 5 + Mockito로 비즈니스 로직 검증
- **도메인 테스트**: 각 도메인의 비즈니스 로직에 대한 단위 테스트

---

## 🛠️ 주요 기능

### **재입고 알림 전송**

- 상품 재고가 존재할 때 알림 메시지를 설정된 사용자에게 전송합니다.

### **재입고 알림 재전송 (수동)**

- 예외로 중단된 알림 전송을 마지막 성공 유저 이후부터 재시작합니다.

### **상태 관리**

- 알림 상태: `IN_PROGRESS`, `COMPLETED`, `CANCELED_BY_SOLD_OUT`, `CANCELED_BY_ERROR`

---

## 📧 문의

- **개발자**: 노재혁
- **GitHub**: [[프로젝트 링크](https://github.com/NoJaeHyuk/realarm-product)]
