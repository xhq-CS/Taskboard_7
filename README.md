# Taskboard - Homework 7
For Homework 7, the API was updated with advanced backend features:
- Custom exceptions
- Global exception handling
- Error response DTO
- Task request and response DTOs
- Soft delete
- Restore deleted tasks
- Request/response logging
- Spring Boot Actuator health endpoint

---

# EXCEPTION HANDLING
The Campus Taskboard now includes custom exceptions:

TaskNotFoundException
- Used when a task ID does not exist.

InvalidTaskDataException
- Used when task data is invalid.

GlobalExceptionHandler
- Handles errors in one central place using @RestControllerAdvice.

Error responses now include:
- timestamp
- status
- error
- message
- path

---

# DTOs
TaskRequest
- Used for incoming task data from the client.

TaskResponse
- Used for outgoing task data sent back to the client.

ErrorResponse
- Used for consistent error messages.

With the addition of DTOs it will help separate the API request and response structure from the database entity.

---

# SOFT DELETE
Tasks will no longer be permanently deleted. It will now remove the task from the database, and the API sets: deleted = true. While only temporarily removed or hidden from plain view.

Normal GET requests only return tasks where deleted is false.

Restore endpoint:
- PUT /api/tasks/{id}/restore
This also sets deleted category back to false.

---
# REQUEST LOGGING
- HTTP method
- Request URI
- Response status
- Request duration

---

# HEALTH ENDPOINT
Uses the bult-in Spring Boot Actuator to monitor health/status of database.


Health endpoint:
- GET /actuator/health

Expected response if reachable:

{

  "status": "UP"
  
}

---

# HOMEWORK 7 SCREENSHOTS INCLUDED
- 404 error response
- 400 validation error response
- 500 internal server error response
- Soft delete working
- Health endpoint working
- Request logging in console

---

# VIDEO LINK
https://drive.google.com/file/d/1Ue1akjUq4qqTelm6HFGNB719orFBuiVI/view?usp=sharing
