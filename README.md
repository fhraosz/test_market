## ⚙️ 구현 설명

이 프로젝트는 **Spring Boot 3.x**, **JPA**, **MySQL(Docker)** 기반으로 “캐시노트 마켓” 주문/결제 서비스를 구현했습니다.  
주요 기능은 다음과 같습니다.

- **주문 생성**  
  - 회원 검증 → 상품 가격 검증(프론트에서 전달된 총액 비교) → 주문 저장  
  - 주문 항목별 `unit_price`를 별도로 보존  
- **복합 결제 처리**  
  - PG(KPN) + 포인트/쿠폰 조합  
  - BNPL(외상결제)은 단독 결제만 지원  
  - 각 결제 전략(`PaymentStrategy`)을 전략 패턴으로 분리  
  - 결제 성공/실패 시 주문 상태 자동 업데이트  
- **예외/로그 관리**  
  - `OrderException` 전용 예외 + 전역 예외 핸들러  
  - 단계별 상세 `log.debug`/`log.error` 출력

---

- **Layered Architecture**:  
  - Controller → Application(Service) → Domain(Entity/Service) → Repository  
- **전략 패턴**: 결제 수단별 로직을 `PaymentStrategy`로 분리 → 결제 수단 추가/변경 시 확장성 확보  
- **DTO → Entity 변환**: 정적 팩토리 메서드(`.of()`, `.from()`)를 활용해 가독성 및 캡슐화 강화  
- **트랜잭션 경계**: `@Transactional`을 통해 주문 생성과 결제 저장을 원자적으로 처리

- 

## 🤔 설계 고민 및 선택

1. **복합 결제 vs 단일 책임**  
   - 한 주문에 여러 결제수단(주요 PG + 보조 포인트/쿠폰)을 허용해야 했음  
   - 전략 패턴으로 결제 로직을 분리하여, 새로운 결제 수단(BNPL 등) 도입을 용이하게 설계  
2. **데이터 일관성**  
   - 결제 중간 실패 시 주문 전체를 취소하고 롤백할지 고민  
   - 트랜잭션 구간을 좁히고, 실패 즉시 `OrderException`을 던져 깔끔히 처리하도록 구현  
3. **가격 검증 시점**  
   - 프론트 전달 `totalAmount`를 신뢰할 수 없어, 서버에서 상품별 가격을 재계산하여 비교  
   - `unit_price`를 `ORDER_ITEM`에 보존해 향후 가격 변경에도 주문 기록 보존

## 🛠️ Prerequisites

- **Git**  
- **Docker & Docker Compose** (v1.27+ 권장)  
- **Java 18+** (로컬 실행 시)  
- **Gradle Wrapper** (`./gradlew`)

---

## 📥 소스 코드 클론

```bash
git clone https://github.com/fhraosz/test_market.git
cd test_market
```

---

## 🚀 앱 빌드 & 컨테이너 실행

1. JAR 빌드

    ```bash
    ./gradlew bootJar
    ```

2. Docker 이미지 빌드

    ```bash
    docker build -t market-order-app .
    ```

3. Docker Compose 구동

    ```bash
    docker-compose up -d
    ```

---

## 🔍 실행 로그 확인

```bash
docker-compose logs -f app
```

---

## 📋 API 명세

모든 요청/응답은 `application/json` 으로 처리합니다.  
기본 URL: `http://{host}:{port}/api`

### 1. 주문 생성 (Create Order)

- **URL**: `POST /order/create`  
- **설명**: 고객의 주문 요청을 받아 주문을 생성하고, 결제까지 처리합니다.  
- **요청 헤더**:

    | Header            | Value               |
    |-------------------|---------------------|
    | `Content-Type`    | `application/json`  |

- **요청 바디**:

    ```json
    {
      "memberId": 1,
      "totalAmount": 1900000,
      "orderItemDtoList": [
        { "productId": 101, "quantity": 1 },
        { "productId": 102, "quantity": 1 }
      ],
      "paymentDtoList": [
        { "type": "PG",    "amount": 1500000, "isMain": true  },
        { "type": "POINT", "amount":  400000, "isMain": false }
      ]
    }
    ```

    | 필드               | 타입         | 설명                                      |
    |--------------------|--------------|-------------------------------------------|
    | `memberId`         | Long         | 주문자 회원 ID                            |
    | `totalAmount`      | BigDecimal   | 프론트에서 계산해 보낸 주문 총액          |
    | `orderItemDtoList` | Array        | 주문 항목 목록                            |
    | └─ `productId`     | Long         | 상품 ID                                   |
    | └─ `quantity`      | Integer      | 수량                                      |
    | `paymentDtoList`   | Array        | 결제 수단 목록                            |
    | └─ `type`          | String       | 결제 타입 (`PG`, `POINT`, `COUPON`, `BNPL`) |
    | └─ `amount`        | BigDecimal   | 해당 수단으로 결제할 금액                 |
    | └─ `isMain`        | Boolean      | 메인 결제 수단 여부                      |

- **응답 예시 (성공)**: HTTP 201 Created

    ```json
    {
      "orderId": 123,
      "status": "PAID",
      "createdAt": "2025-07-27T15:00:00",
      "payments": [
        { "success": true, "code": "tx-20250727-001", "message": "결제 성공" },
        { "success": true, "code": "",               "message": "결제 성공" }
      ]
    }
    ```

    | 필드         | 타입                | 설명                                    |
    |--------------|---------------------|-----------------------------------------|
    | `orderId`    | Long                | 생성된 주문 ID                          |
    | `status`     | String              | 주문 상태 (`CREATED`, `PAID`, `CANCELED`) |
    | `createdAt`  | String (ISO‑8601)   | 주문 생성 시각                          |
    | `payments`   | Array               | 개별 결제 결과 목록                     |
    | └─ `success` | Boolean             | 결제 성공 여부                          |
    | └─ `code`    | String              | 거래 ID 또는 에러 코드                  |
    | └─ `message` | String              | 사람 읽을 수 있는 상태 메시지           |

- **오류 응답 예시**: HTTP 400 Bad Request

    ```json
    { "message": "회원 정보를 찾을 수 없습니다." }
    { "message": "유효하지 않은 주문 금액입니다." }
    { "message": "주문 처리에 실패했습니다." }
    ```

---

## 🗄️ 데이터베이스 ERD

    MEMBER {
      BIGINT   id             PK "회원 ID"
      VARCHAR  name            "회원 이름"
      VARCHAR  email           "이메일"
      DATETIME created_at      "생성 일시"
      DATETIME updated_at      "수정 일시"
    }

    PRODUCT {
      BIGINT   id             PK "상품 ID"
      VARCHAR  name            "상품명"
      DECIMAL  price           "단가"
      DATETIME created_at      "생성 일시"
      DATETIME updated_at      "수정 일시"
    }

    ORDER {
      BIGINT   id             PK "주문 ID"
      BIGINT   member_id      FK → MEMBER.id "주문자"
      DECIMAL  total_amount    "총 주문 금액"
      VARCHAR  status          "주문 상태 (CREATED/PAID/CANCELED)"
      DATETIME created_at      "생성 일시"
      DATETIME updated_at      "수정 일시"
    }

    ORDER_ITEM {
      BIGINT   id             PK "주문 항목 ID"
      BIGINT   order_id       FK → ORDER.id  "주문 참조"
      BIGINT   product_id     FK → PRODUCT.id "상품 참조"
      INT      quantity        "수량"
      DECIMAL  unit_price      "주문 시 단가"
      DATETIME created_at      "생성 일시"
      DATETIME updated_at      "수정 일시"
    }

    PAYMENT {
      BIGINT   id             PK "결제 ID"
      BIGINT   order_id       FK → ORDER.id  "주문 참조"
      VARCHAR  payment_type    "결제 수단 (PG/POINT/COUPON/BNPL)"
      DECIMAL  amount          "결제 금액"
      BOOLEAN  is_main         "메인 결제 수단 여부"
      VARCHAR  status          "결제 상태 (SUCCESS/FAILED)"
      VARCHAR  transaction_id  "거래 또는 에러 코드"
      DATETIME created_at      "생성 일시"
      DATETIME updated_at      "수정 일시"
      INT      points_used     "사용 포인트 (POINT)"
      VARCHAR  coupon_code     "쿠폰 코드 (COUPON)"
      DECIMAL  coupon_amount   "쿠폰 할인 금액 (COUPON)"
    }
```
