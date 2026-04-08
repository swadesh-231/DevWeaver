# DevWeaver 🧵

DevWeaver is a modern, collaborative platform designed to "weave" together development, AI assistance, and team collaboration. Built with a robust Spring Boot backend, it provides a unified space for managing projects, collaborating with team members, and leveraging AI to accelerate the development lifecycle.

## 🚀 Features

- **Project Management**: Effortlessly create, update, and manage your development projects.
- **AI-Powered Collaboration**: Integrated chat sessions with AI assistance, supporting tool calls and usage tracking.
- **Team Synergy**: Add members to projects and work together in real-time.
- **File Management**: Securely handle project-related files and assets.
- **Subscription Support**: Built-in tiered subscription system to scale with your needs.
- **Extensible Architecture**: Clean, hexagonal-inspired architecture for easy maintenance and scalability.

## 🛠️ Technology Stack

- **Framework**: Spring Boot 4.0.4
- **Language**: Java 21
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **ORM**: Spring Data JPA
- **Utilities**: Lombok, Dotenv for configuration, Spring Security (Planned Integration)

## 🏗️ Getting Started

### Prerequisites

- Java 21+
- PostgreSQL
- Gradle (Optional, wrapper included)

### Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/your-username/DevWeaver.git
    cd DevWeaver
    ```

2.  **Configure Environment**:
    Create a `.env` file in the root directory based on `.env.example`:
    ```env
    DB_URL=jdbc:postgresql://localhost:5432/devweaver
    DB_USERNAME=your_username
    DB_PASSWORD=your_password
    ```

3.  **Run with Gradle**:
    ```bash
    ./gradlew bootRun
    ```

## 📂 Project Structure

- `src/main/java/com/devweaver/controller`: REST API endpoints.
- `src/main/java/com/devweaver/service`: Core business logic.
- `src/main/java/com/devweaver/repository`: Data access layer.
- `src/main/java/com/devweaver/entity`: JPA entities and data models.
- `src/main/java/com/devweaver/dto`: Data Transfer Objects for API communication.
- `src/main/java/com/devweaver/config`: System configuration.

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---
Built with ❤️ by the DevWeaver Team.
