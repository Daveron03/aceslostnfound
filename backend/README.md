# Campus Lost & Found API

Spring Boot REST API backend for the Campus Lost & Found Portal.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Quick Start

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Or build and run the JAR:**
   ```bash
   mvn clean package
   java -jar target/lost-found-api-1.0.0.jar
   ```

The API will start on `http://localhost:8080`

## API Endpoints

### Items

- `GET /api/items` - Get all items (newest first)
- `GET /api/items/lost` - Get lost items only
- `GET /api/items/found` - Get found items only
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create new lost item
- `PUT /api/items/{id}/found` - Mark item as found
- `DELETE /api/items/{id}` - Delete item
- `GET /api/items/stats` - Get item statistics

### Request/Response Examples

**Create Item (POST /api/items):**
```json
{
  "title": "Blue Backpack",
  "description": "Lost near library, has laptop inside",
  "photoUrl": "https://example.com/photo.jpg"
}
```

**Item Response:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "title": "Blue Backpack",
  "description": "Lost near library, has laptop inside",
  "photoUrl": "https://example.com/photo.jpg",
  "status": "LOST",
  "createdAt": "2025-07-14T10:30:00Z"
}
```

## Database

- **Development:** H2 in-memory database
- **H2 Console:** Available at `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:lostfound`
  - Username: `sa`
  - Password: (empty)

## Configuration

Edit `src/main/resources/application.properties` for:
- Database settings
- CORS origins
- Server port
- Logging levels

## CORS

Configured to allow requests from:
- `http://localhost:5173` (Vite default)
- `http://localhost:3000` (Create React App default)

## Production Setup

For production deployment:

1. **Add MySQL configuration:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lostfound
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

2. **Update CORS origins:**
   ```properties
   cors.allowed-origins=https://your-domain.com
   ```

3. **Build production JAR:**
   ```bash
   mvn clean package -Dmaven.test.skip=true
   ```
