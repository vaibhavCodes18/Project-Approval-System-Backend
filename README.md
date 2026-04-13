# Project Hub: Backend (Spring Boot)

This is the backend service for the Project Approval System, built using Spring Boot and MongoDB. It provides a secure, role-based API for managing project lifecycles, user authentication, and organizational workflows.

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.x
- **Language**: Java 17
- **Database**: MongoDB
- **Security**: Spring Security with JWT (Stateless)
- **Persistence**: Spring Data MongoDB
- **Mapping & Utilities**: Lombok, Jakarta Validation, JJWT (io.jsonwebtoken)

---

## 📂 Architecture & Folder Structure

The project follows a standard layered architecture:

```text
src/main/java/com/bit/ProjectApprovalSystem/
├── config/             # Configuration for CORS, MongoDB indexing, and database connection.
├── controller/         # REST Controllers exposing endpoints for Dashboard, Auth, Projects, etc.
├── dto/                # Data Transfer Objects for decoupled request and response mapping.
├── entity/             # MongoDB Collection models (User, Project, ProjectMember, Approval, RefreshToken).
├── enums/              # Strong typing for Statuses and Roles.
├── exception/          # Global Exception Handling and custom application exceptions.
├── repository/         # Repository interfaces extending MongoRepository.
├── security/           # Core security logic: JwtService, JWT Filter, and SecurityFilterChain.
├── service/            # Business logic layer (Service Interfaces and Impl).
└── utils/              # Helper utilities and UserDetailsService.
```

---

## 🔒 Security & Authorization

- **JWT Authentication**: Secure stateless authentication using `Authorization: Bearer <token>` headers.
- **Refresh Token Pattern**: Implements a secure rotation strategy for access and refresh tokens.
- **Role-Based Access Control (RBAC)**: 
  - `HOD`: Full administrative access, user state management, and final project approval.
  - `GUIDE`: Access to assigned projects and approval/rejection capability.
  - `STUDENT`: Project creation, member management, and submission.

---

## 📊 Core Entities (MongoDB)

### User
Manages all participants. Role determines dashboard access and permissions.
### Project
The central entity tracking title, description, and lifecycle status (`DRAFT` to `HOD_APPROVED`).
### ProjectMember
Maps students to specific projects with assigned roles (Leader/Member).
### Approval
Stores the history of approvals/rejections from both Guides and HODs, including timestamped remarks.

---

## ⚙️ Setup & Configuration

### Prerequisites
- JDK 17+
- MongoDB 6.0+
- Maven 3.8+

### Environment Configuration
The application consumes configuration from `src/main/resources/application.properties`. Ensure the following properties are set:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/project_approval_db
jwt.secret=your_super_secret_high_entropy_key_here
jwt.expiration=86400000
```

### Running Locally
```bash
# Clean and compile
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be accessible at: `http://localhost:8080/api/v1`
