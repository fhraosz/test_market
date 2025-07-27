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



