# SmartScan-AI-Evaluator
SmartScan AI Evaluator is a web-based system that uses OCR and Generative AI to automatically evaluate both objective and handwritten subjective exam answers with semantic understanding.  It reduces manual effort, eliminates bias, and provides consistent, instant feedback to students.
An AI-powered answer sheet evaluation system that automatically analyzes student responses, compares them with model answers, and generates scores, similarity, and feedback using OCR and AI techniques.
🚀 Tech Stack
🎨 Frontend
React.js

HTML5

CSS3

JavaScript

⚙️ Backend
Java

Spring Boot

REST APIs

🗄️ Database
MySQL

🧠 AI / Processing Tools
Ollama (LLM)
👉 Generates feedback and LLM-based score by comparing student and model answers

OCR (Text Extraction)
👉 Extracts text from scanned answer sheets (PDF/Image)

Tesseract OCR
👉 Core engine used to convert images into text

OpenCV
👉 Preprocesses images (noise removal, sharpening, grayscale) for better OCR accuracy

Embedding Service
👉 Converts text into vectors and calculates similarity between answers

🎯 Features
User Registration & Login (Admin & Teacher)

Role-based access control

Upload student answer sheets (PDF/Image)

OCR-based text extraction

AI-based evaluation (LLM + similarity)

Final score calculation

Feedback generation

Processing status tracking

Dashboard & analytics

🧠 How It Works
Teacher logs in

Creates model answer

Uploads student answer sheet

System stores file

OCR extracts text using Tesseract

OpenCV preprocesses image for better accuracy

Extracted text is cleaned

Embedding service calculates similarity

Ollama (LLM) generates:

Score

Feedback

Final score is calculated

Results are stored and shown in dashboard

📊 Score Calculation
Similarity Score = similarity × 10  
Final Score = (LLM Score + Similarity Score) / 2
🔌 APIs Used
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

📁 Project Structure
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
▶️ How to Run
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
npm install
npm start
5. Open Browser
http://localhost:3000
🔄 Workflow
Login → Create Model → Upload Sheet  
→ OCR → Text Extraction  
→ AI Evaluation (Ollama + Embedding)  
→ Score + Feedback → Dashboard
