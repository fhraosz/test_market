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

## 🚀 앱 빌드 & 컨테이너 실행

# 1) JAR 빌드
./gradlew bootJar

# 2) Docker 이미지 빌드
docker build -t market-order-app .

# 3) Docker Compose 구동
docker-compose up -d

## 🔍 실행 로그 확인

docker-compose logs -f app

# 📋 API 명세

모든 요청/응답은 `application/json` 으로 처리합니다.  
기본 URL: `http://{host}:{port}/api`

---

## 1. 주문 생성 (Create Order)

- **URL**  
  `POST /order/create`
- **설명**  
  고객의 주문 요청을 받아 주문을 생성하고, 결제까지 처리합니다.
- **요청 헤더**  
  | Header               | Value                   |
  |----------------------|-------------------------|
  | `Content-Type`       | `application/json`      |
- **요청 바디**  
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

| 필드                 | 타입         | 설명                                      |
| ------------------ | ---------- | --------------------------------------- |
| `memberId`         | Long       | 주문자 회원 ID                               |
| `totalAmount`      | BigDecimal | 프론트에서 계산해 보낸 주문 총액                      |
| `orderItemDtoList` | Array      | 주문 항목 목록                                |
| └─ `productId`     | Long       | 상품 ID                                   |
| └─ `quantity`      | Integer    | 수량                                      |
| `paymentDtoList`   | Array      | 결제 수단 목록                                |
| └─ `type`          | String     | 결제 타입 (`PG`, `POINT`, `COUPON`, `BNPL`) |
| └─ `amount`        | BigDecimal | 해당 수단으로 결제할 금액                          |
| └─ `isMain`        | Boolean    | 메인 결제 수단 여부                             |

응답 예시 (성공)

HTTP 201 Created
{
  "orderId": 123,
  "status": "PAID",
  "createdAt": "2025-07-27T15:00:00",
  "payments": [
    { "success": true, "code": "tx-20250727-001", "message": "결제 성공" },
    { "success": true, "code": "",               "message": "결제 성공" }
  ]
}

| 필드           | 타입                | 설명                                    |
| ------------ | ----------------- | ------------------------------------- |
| `orderId`    | Long              | 생성된 주문 ID                             |
| `status`     | String            | 주문 상태 (`CREATED`, `PAID`, `CANCELED`) |
| `createdAt`  | String (ISO‑8601) | 주문 생성 시각                              |
| `payments`   | Array             | 개별 결제 결과 목록                           |
| └─ `success` | Boolean           | 결제 성공 여부                              |
| └─ `code`    | String            | 거래 ID 또는 에러 코드                        |
| └─ `message` | String            | 사람 읽을 수 있는 상태 메시지                     |


오류 응답 예시

400 Bad Request

{ "message": "회원 정보를 찾을 수 없습니다." }
{ "message": "유효하지 않은 주문 금액입니다." }
{ "message": "주문 처리에 실패했습니다." }
