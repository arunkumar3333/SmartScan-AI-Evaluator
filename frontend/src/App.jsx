import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import TeacherDashboardPage from "./pages/TeacherDashboardPage";
import AdminDashboardPage from "./pages/AdminDashboardPage";
import NotFoundPage from "./pages/NotFoundPage";
import ProtectedRoute from "./components/ProtectedRoute";

import ProcessingStatusPage from "./pages/ProcessingStatusPage";
import OcrResultPage from "./pages/OcrResultPage";
import EvaluationResultPage from "./pages/EvaluationResultPage";
import TeacherUploadHistoryPage from "./pages/TeacherUploadHistoryPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />

        <Route path="/teacher-upload-history" element={<TeacherUploadHistoryPage />} />
        <Route path="/teacher-dashboard" element={<TeacherDashboardPage />} />
        <Route path="/processing-status/:answerSheetId" element={<ProcessingStatusPage />} />
        <Route path="/ocr-result/:answerSheetId" element={<OcrResultPage />} />
        <Route path="/evaluation-result/:answerSheetId" element={<EvaluationResultPage />} />

        <Route
          path="/teacher"
          element={
            <ProtectedRoute allowedRole="TEACHER">
              <TeacherDashboardPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRole="ADMIN">
              <AdminDashboardPage />
            </ProtectedRoute>
          }
        />

        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;