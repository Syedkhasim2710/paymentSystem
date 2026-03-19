# 💳 Payment Orchestration System

A robust, production-ready Multi-Gateway Payment System built with **Spring Boot 3.4**, **Spring Security 6**, and **JPA**. This project demonstrates high-level architectural patterns used in top-tier e-commerce systems to handle diverse payment methods, security, and callbacks.

---

## 🏗️ Architectural Highlights

### 1. Design Patterns Used

| Pattern | Description |
|---|---|
| **Strategy Pattern** | Encapsulates gateway-specific logic (Razorpay, Stripe) into interchangeable classes, making the system highly extensible. |
| **Factory Pattern** | Dynamically resolves the correct gateway strategy at runtime, decoupling the service layer from concrete implementations. |
| **Adapter Pattern** | Transforms diverse provider payloads into a unified `PaymentTransaction` format. |
| **Template Method** | Defines a standard execution flow (Initiate → Process → Audit). |

### 2. SOLID Principles Applied

| Principle | Implementation |
|---|---|
| **S** — Single Responsibility | Dedicated classes for Authentication, Controllers, Business Logic, and Gateway execution. |
| **O** — Open/Closed | Add a new gateway (e.g., PayPal or Crypto) by simply adding a new Strategy class — no modification of existing factory or service code. |
| **L** — Liskov Substitution | All gateway strategies strictly adhere to the `PaymentGatewayStrategy` interface. |
| **I** — Interface Segregation | Strategies only implement methods relevant to payment processing and refunds. |
| **D** — Dependency Inversion | High-level services depend on the `PaymentGatewayStrategy` abstraction, not concrete implementations. |

---

## 🛡️ Security Features

- **Stateless Authentication:** Uses Spring Security 6 with `SessionCreationPolicy.STATELESS` for high-performance API scaling.
- **Webhook Protection:** A custom `OncePerRequestFilter` validates the `X-API-KEY` header for all incoming provider callbacks, preventing unauthorized status updates.
- **Generic Data Isolation:** Sensitive provider-specific data is stored in a `metadata` JSON field, separating core financial columns from dynamic provider data.

---

## 🚀 API Endpoints & Testing (CURL)

### 1. Initiate a Payment

Triggers the internal factory and creates a transaction record in `INITIATED` status.

```bash
curl -X POST http://localhost:8080/api/payments/initiate \
-H "Content-Type: application/json" \
-d '{
  "orderId": "ORD_2026_001",
  "amount": 1250.00,
  "currency": "INR",
  "gateway": "RAZORPAY",
  "metadata": {"vpa": "user@okaxis", "cust_email": "user@example.com"}
}'
```

> 📌 Take note of the `id` (UUID) returned in the response — it will be used as `{TXN_ID}` in subsequent calls.

---

### 2. Check Payment Status

Get the current state of any transaction (initially `INITIATED`).

```bash
curl -X GET http://localhost:8080/api/payments/{TXN_ID}
```

---

### 3. Secure Webhook Callback

Simulates the Bank/Gateway notifying your server of a successful charge. **Requires the `X-API-KEY` header.**

```bash
curl -X POST http://localhost:8080/api/payments/webhook/callback \
-H "X-API-KEY: FLIPKART_SECRET_2026" \
-H "Content-Type: application/json" \
-d '{
  "merchant_tid": "{TXN_ID_FROM_PREVIOUS_STEP}",
  "status": "SUCCESS",
  "bank_reference": "BANK_REF_999"
}'
```

---

### 4. Process a Refund

Initiates a reversal through the original gateway's strategy. Only applicable when status is `SUCCESS`.

```bash
curl -X POST "http://localhost:8080/api/payments/{TXN_ID}/refund?amount=1250.00"
```

---

### 5. Administrative Status Update

A direct patch to force a status change (e.g., for manual reconciliation).

```bash
curl -X PATCH "http://localhost:8080/api/payments/{TXN_ID}/status?status=FAILED"
```

---

## ⚙️ Configuration & Setup

### Requirements

- **Java 17+**
- **Maven 3.8+**

### Dependencies

| Dependency | Purpose |
|---|---|
| Spring Web | REST API layer |
| Spring Data JPA | ORM & database operations |
| Spring Security 6 | Stateless API Key authentication |
| Lombok | Boilerplate reduction |
| H2 | In-memory database for local dev |
| SpringDoc OpenAPI | Swagger UI auto-generation |

### Database & Documentation

| Resource | URL |
|---|---|
| **H2 Console** | `http://localhost:8080/h2-console` |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` |

> **H2 Login:** JDBC URL: `jdbc:h2:mem:paymentdb` · Username: `sa` · Password: `password`

### `application.properties` Highlights

```properties
payment.webhook.secret=FLIPKART_SECRET_2026
spring.datasource.url=jdbc:h2:mem:paymentdb
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```

---

## 🧠 Future Scalability (Roadmap)

- **Distributed Idempotency:** Implement Redis-based locks to prevent race conditions during concurrent payment hits. The `orderId` should carry a unique DB constraint to prevent duplicate charges.
- **Saga Pattern:** Add compensating transactions (auto-refunds) if downstream microservices (Inventory/Order) fail during checkout.
- **Reconciliation Engine:** A scheduled Cron job to poll Gateway APIs for transactions stuck in `INITIATED` status due to network timeouts.
- **Observability:** Integrate **Sleuth/Zipkin** for distributed tracing to track a single payment across the Gateway, Bank, and Ledger services.
- **Gateway Scaling:** Move `PaymentGatewayFactory` configuration to a distributed config store (e.g., Consul or Spring Cloud Config) when the number of gateways grows into the hundreds.

---

## 📐 Project Structure

```
src/
└── main/
    ├── java/com/pay/
    │   ├── PaymentApplication.java
    │   ├── config/
    │   │   ├── ApiKeyFilter.java          # OncePerRequestFilter for X-API-KEY
    │   │   └── SecurityConfig.java        # Stateless Spring Security 6 config
    │   ├── controller/
    │   │   ├── PaymentController.java     # Core payment REST endpoints
    │   │   └── WebhookController.java     # Secure webhook callback handler
    │   ├── model/
    │   │   ├── PaymentGateway.java        # Enum: RAZORPAY, STRIPE, PAYPAL
    │   │   ├── PaymentStatus.java         # Enum: INITIATED, SUCCESS, FAILED, REFUNDED
    │   │   ├── PaymentTransaction.java    # JPA entity with metadata JSON column
    │   │   └── dto/
    │   │       └── PaymentRequest.java    # Inbound request DTO
    │   ├── repo/
    │   │   └── PaymentRepository.java     # Spring Data JPA repository
    │   └── service/
    │       ├── PaymentGatewayFactory.java # Factory: resolves strategy by gateway enum
    │       ├── PaymentService.java        # Core orchestration & business logic
    │       └── strategy/
    │           ├── PaymentGatewayStrategy.java   # Interface (abstraction)
    │           ├── RazorpayStrategy.java          # Razorpay implementation
    │           ├── StripeGatewayStrategy.java     # Stripe implementation
    │           └── PaypalGatewayStrategy.java     # PayPal implementation
    └── resources/
        └── application.properties
```

