## 📘 Book Store Backend – Java Spring Boot Project

Welcome to the Book Store Backend, a scalable and modular RESTful API built using Java and the Spring Boot framework.
This project is designed to support a full-featured online bookstore, providing endpoints for managing users, books,
orders, and more, with robust security and documentation in place.

The application follows a Layered Architecture to promote clean code separation, maintainability, and extensibility.
It leverages modern backend technologies and tools to streamline development, testing, deployment, and security.

---

[▶️ Watch the demo video](https://www.loom.com/share/e67852eadf2c427c880753bec831e4f3)


---

### Key Technologies

- **Spring Boot** – Rapid application development with embedded server support and production-ready configurations.
- **Spring Security** – Handles authentication and authorization with JWT integration.
- **Spring Data JPA (Hibernate)** – Simplifies database interactions using object-relational mapping.
- **MySQL** – Reliable and performant relational database for persistent data storage.
- **Swagger (OpenAPI)** – Automatically generated and interactive API documentation.
- **Postman** – API testing and validation tool.
- **Docker** – Containerization for consistent deployment across environments.

---

### Architecture Overview

- This project implements a Layered Architecture:
- Controller Layer: Exposes RESTful endpoints and handles HTTP requests/responses.
- Service Layer: Contains core business logic and mediates between controllers and repositories.
- Repository Layer: Interacts with the database using Spring Data JPA.
- Model Layer: Defines domain entities and data transfer objects (DTOs).
- Security Layer: Implements JWT-based authentication and role-based access control.

---

### Features & Functionalities

The system is built on a RESTful architecture and includes the following main controllers:

**AuthenticationController**

- **POST: `/registration`** - Register new users (with role USER)
- **POST: `/login`** - Authenticate existing users with JWT

**BookController**

- **POST: `/books`** - Create a new book (only for role ADMIN)
- **GET: `/books`** - View list all available books
- **GET: `/books/1`** - View a book with id 1
- **PUT: `/books/1`** - Update a book with id 1 (only for role ADMIN)
- **DELETE: `/books/1`** - Mark as deleted a book with id 1 (only for role ADMIN)
- **GET: `/books/search`** - Filter books by: isbn, title, author

**OrderController**

- **POST: `/orders`** - Create a new order (only for role ADMIN)
- **GET: `/orders`** - View list all available orders
- **GET: `/orders/1`** - View an order with id 1
- **GET: `/orders/1/items/2`** - View an item with id 2 in the order with id 1
- **PATCH: `/orders/1`** - Change status order with id 1 (only for role ADMIN)

**CategoryController**

- **POST: `/categories`** - Create a new category (only for role ADMIN)
- **GET: `/categories`** - View list all available categories
- **GET: `/categories/1`** - View a category with id 1
- **PUT: `/categories/1`** - Update a category with id 1 (only for role ADMIN)
- **DELETE: `/categories/1`** - Mark as deleted a category with id 1 (only for role ADMIN)
- **GET: `/categories/1/books`** - View list of books with category id 1

**ShoppingCartController**

- **POST: `/cart`** - Add the item to shopping cart
- **GET: `/cart`** - View all items in the shopping cart
- **PUT: `/cart/items/1`** - Update the quantity item with id 1 in the shopping cart
- **DELETE: `/cart/1`** - Delete the item with id 1 in shopping cart

---

### Database Schema Relationship Diagram

![scheme](scheme-book-store.png)

---

### User Roles and API Usage

This project supports two main roles: Admin and User. Each role has access to different endpoints and operations.

#### Getting Started with API with Postman

Make sure Postman is installed on your local machine before starting API testing.

+ To get started, you must authenticate as an admin and obtain a JWT token.

*Method:* **POST** `http://localhost:8088/api/auth/login`

*Body:*

```
{
"email": "admin@email.com", 
"password": "1234"
}
 ```

+ After you authenticate and get a JWT token, you can create a category.
+ Don’t forget add your token in the Authorization header as a Bearer Token every time.

*Method* **POST** `http://localhost:8088/api/categories`

*Body:*

```
{
"name": "Classic", 
"description": "Good books"
}
```

+ Once you've created a category, you can add a new book.

*Method* **POST** `http://localhost:8088/api/books`

*Body*

```
{
"title": "Kobzar",
"author": "Shevchenko",
"isbn": "1234567890",
"price": 123.45,
"description": "A good book",
"coverImage": "example.com/cover.jpg",
"categories": [1]
} 
```

**Also as an Admin you can:**

+ View all books
+ Get book details by ID
+ Create, update, or delete books
+ Search books by parameters
+ Manage categories: create, update, delete, view
+ Manage orders: view all, update status


+ To continue testing as a regular user, you need to register a new account.

*Method* **POST** `http://localhost:8088/api/auth/registration`

*Body*

```
{
"email": "bob.doe@example.com",
"password": "12345",
"repeatedPassword": "12345",
"firstName": "Bob",
"lastName": "Doe",
"shippingAddress": "123 Main St, City"
}
```

+ After registering a new user, you need to log in to obtain a JWT token.

*Method* **POST** `http://localhost:8088/api/auth/login`

*Body*

```
{
"email": "bob.doe@example.com",
"password": "12345"
}
```

+ After getting the token, you can add a book to your shopping cart
+ Just don’t forget to include the token in the Authorization header as a Bearer token every time.

*Method* **POST** `http://localhost:8088/api/cart`

*Body* 
```
{
"bookId": 1,
"quantity": 1
}
```

+ Before creating an order, you can review your shopping cart to ensure everything is correct.

*Method* **GET** `http://localhost:8088/api/cart`

+ If something is wrong with your selection, you can delete books from your shopping cart or change their quantity.

*Method* **DELETE** `http://localhost:8088/api/cart/{book_id}`

*Method* **PUT** `http://localhost:8088/api/cart//items/{book_id}`

*Body*
```
{
"quantity": 3
}
```

+ Now we can place an order.

*Method* **POST** `http://localhost:8088/api/orders`

*Body* 
```
{
"shippingAddress": "12 Main St. Kyiv"
}
```

+ We can review all our orders.

*Method* **GET** `http://localhost:8088/api/orders`

**Also as a User you can:**

+ Review all books
+ Get books by ID or search by parameters
+ Review all categories
+ Get books by ID
+ Get books by category
+ Get order by ID

---

You can also test this API using Swagger by accessing the following link:

[Link to Swagger: BookStore.](http://ec2-44-211-209-132.compute-1.amazonaws.com/api/swagger-ui/index.html#/)

---