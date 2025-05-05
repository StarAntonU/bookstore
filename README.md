# 📚 Book Store Application
Welcome to the Book Store Application – a robust, secure, and scalable backend system designed for managing books, users, and orders in an online bookstore environment.

Whether you're building a personal reading archive or a full-scale e-commerce platform, this project demonstrates a modern and efficient architecture using Spring Boot and other powerful tools.

# 🚀 Motivation
The idea for this project was born out of the need for a clean, maintainable, and extendable backend for a book-selling platform. Many online bookstore systems are either overly simplistic or too complex for new developers to grasp. This project strikes a balance—offering secure endpoints, role-based access, and intuitive design using modern Spring technologies.

It solves key problems such as:

Managing book inventories with ease.

Securing APIs with JWT-based authentication.

Handling user roles (Admin, Customer).

Providing easy API documentation and testing.

# 🛠️ Technologies Used
Tool/Library	Purpose
Spring Boot	Core application framework
Spring Security	Authentication & Authorization (JWT)
Spring Data JPA	ORM for interacting with MySQL
MySQL	Relational database
Swagger (OpenAPI)	API documentation
Docker	Containerization for deployment
Postman	API testing and collection export

# 🧭 Application Structure & Features
🔒 Authentication & Authorization
JWT-based login & signup.

Role-based access control (Admin, Customer).

# 📘 Book Management (Admin)
Add, update, delete books.

Upload cover images (optional).

View book inventory.

# 👥 User Features (Customer)
View available books.

Place and track orders.

Maintain user profiles.

# 📦 Order Management
Admin can view all orders.

Users can view their own order history.

# 🧪 Postman Collection
A full set of Postman requests is available in the postman_collection.json file included in this repo.

🔍 How to Use
Open Postman.

Import the postman_collection.json file.

Set your environment variables (e.g., JWT token, base URL).

Test all endpoints with sample data.

# 🧰 How to Set Up the Project
# ⚙️ Prerequisites
Java 17

Maven

Docker (optional)

MySQL (if not using Docker)

# 🐳 Using Docker (Recommended)
Clone the repository:

bash
Copy
Edit
git clone https://github.com/your-repo/bookstore-app.git
cd bookstore-app
Build and run containers:

bash
Copy
Edit
docker-compose up --build
Visit Swagger UI at:
http://localhost:8080/swagger-ui/index.html

# 🔧 Manual Setup (Without Docker)
Configure application.properties with your MySQL credentials.

Run the application:

bash
Copy
Edit
./mvnw spring-boot:run
Access Swagger UI:
http://localhost:8080/swagger-ui/index.html

# ⚠️ Challenges Faced
JWT Integration: Setting up custom filters for stateless JWT security took careful configuration to avoid conflicts with Spring Security defaults.

Database Schema Evolution: Managing schema changes without breaking existing data during development was solved using JPA migrations.

Role-based Access Control: Designing clean endpoints with clearly separated access rights helped maintain clarity and security.

# ✨ Future Enhancements
Integrate payment gateway APIs.

Add book recommendations using collaborative filtering.

Support for book reviews and ratings.

# ✅ Final Thoughts
This project is ideal for developers looking to understand secure, scalable backend development with Spring Boot and Docker. Whether you're learning or deploying it as a base for a real-world product, this system provides a strong foundation.



