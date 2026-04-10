# SmartScan-AI-Evaluator
SmartScan AI Evaluator is a web-based system that uses OCR and Generative AI to automatically evaluate both objective and handwritten subjective exam answers with semantic understanding.  It reduces manual effort, eliminates bias, and provides consistent, instant feedback to students.

An AI-powered answer sheet evaluation system that automatically analyzes student responses, compares them with model answers, and generates scores, similarity, and feedback using OCR and AI techniques.

Tech Stack


Frontend

-React.js

-HTML5

-CSS3

-JavaScript


Backend

-Java

-Spring Boot

-REST APIs


Database

-MySQL

AI / Processing Tools


-Ollama (LLM)

Generates feedback and LLM-based score by comparing student and model answers


-OCR (Text Extraction)

Extracts text from scanned answer sheets (PDF/Image)


-Tesseract OCR

Core engine used to convert images into text


-OpenCV

Preprocesses images (noise removal, sharpening, grayscale) for better OCR accuracy


-Embedding Service

Converts text into vectors and calculates similarity between answers


Features

-User Registration & Login (Admin & Teacher)

-Role-based access control

-Upload student answer sheets (PDF/Image)

-OCR-based text extraction

-AI-based evaluation (LLM + similarity)

-Final score calculation

-Feedback generation

-Processing status tracking

-Dashboard & analytics


Screenshots


Index Page

<img width="1877" height="847" alt="image" src="https://github.com/user-attachments/assets/e9e45d23-1e66-4d70-aabc-e7b990766db6" />





Registration Page

<img width="1207" height="812" alt="image" src="https://github.com/user-attachments/assets/8560d41c-1f40-4344-98fe-c175794a4f7d" />


Login Page

<img width="1833" height="828" alt="image" src="https://github.com/user-attachments/assets/2ef0c09a-ed43-409e-8d6f-cba932ad20b4" />


Teacher Dashboard

<img width="1892" height="870" alt="image" src="https://github.com/user-attachments/assets/181efda2-17a6-4bdb-a766-f06820006123" />


Admin Dashboard

<img width="1877" height="807" alt="image" src="https://github.com/user-attachments/assets/fcf83b60-af8d-462a-883b-43b225525eee" />


Create Model

<img width="1776" height="747" alt="image" src="https://github.com/user-attachments/assets/56a3bb3e-17a9-4644-8c98-a85968feea8c" />


Upload Answer Sheets

<img width="1807" height="772" alt="image" src="https://github.com/user-attachments/assets/402452b6-4dd4-48ac-b3dc-c3ab2f4ee5a9" />


Evaluation Page

<img width="1280" height="871" alt="image" src="https://github.com/user-attachments/assets/42177a83-19bc-479e-849f-8148f672cb9a" />


Uploaded Answer Sheets

<img width="1900" height="797" alt="image" src="https://github.com/user-attachments/assets/a3e3f8a6-0147-477d-a0c5-41742028f18f" />


Result Page

<img width="1900" height="867" alt="image" src="https://github.com/user-attachments/assets/ef66b99f-8c63-4bdc-b56b-f3565c9d2fc5" />


->How It Works


-Teacher logs in

-Creates model answer

-Uploads student answer sheet

-System stores file

-OCR extracts text using Tesseract

-OpenCV preprocesses image for better accuracy

-Extracted text is cleaned

-Embedding service calculates similarity

Ollama (LLM) generates:

-Score

-Feedback

-Final score is calculated

-Results are stored and shown in dashboard


Score Calculation
Similarity Score = similarity × 10  
Final Score = (LLM Score + Similarity Score) / 2


APIs Used

Auth APIs

POST /api/auth/register

POST /api/auth/login


Upload APIs

POST /api/upload

GET /api/upload/all

GET /api/upload/teacher/{teacherId}


Processing APIs

Async processing using @Async

Background evaluation using thread pool


Project Structure

SmartScan-AI-Evaluator/

│

├── backend/

│   ├── controller/

│   ├── service/

│   ├── repository/

│   ├── entity/

│   └── config/

│

├── frontend/

│   ├── pages/

│   ├── components/

│   ├── api/

│   ├── styles/

│   └── App.js

│

└── uploads/


How to Run


1. Setup Database
   
CREATE DATABASE smartscan_db;


2. Configure Backend

spring.datasource.url=jdbc:mysql://localhost:3306/smartscan_db

spring.datasource.username=root

spring.datasource.password=your_password


3. Run Backend

cd backend

mvn spring-boot:run


4. Run Frontend

cd frontend
npm run dev


5. Open Browser

http://localhost:3000


Workflow

Login → Create Model → Upload Sheet  

→ OCR → Text Extraction  

→ AI Evaluation (Ollama + Embedding)  

→ Score + Feedback → Dashboard
