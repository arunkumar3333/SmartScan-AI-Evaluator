import React, { useState } from "react";
import { uploadAnswerSheet } from "../api/uploadApi";
import "../styles/TeacherUploadPage.css";

const TeacherUploadPage = () => {
  const [teacherId, setTeacherId] = useState("");
  const [studentName, setStudentName] = useState("");
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!teacherId || !studentName || !file) {
      setMessage("Please fill all fields and choose a file.");
      return;
    }

    const formData = new FormData();
    formData.append("teacherId", teacherId);
    formData.append("studentName", studentName);
    formData.append("file", file);

    try {
      setLoading(true);
      const response = await uploadAnswerSheet(formData);
      setMessage(`Upload successful: ${response.fileName || "File uploaded"}`);

      setTeacherId("");
      setStudentName("");
      setFile(null);
      e.target.reset();
    } catch (error) {
      setMessage(error.response?.data?.error || "Upload failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="teacher-upload-page">
      <div className="teacher-upload-card">
        <h2 className="teacher-upload-title">Upload Answer Sheet</h2>
        <p className="teacher-upload-subtitle">
          Upload scanned PDF, JPG, JPEG, or PNG files
        </p>

        <form className="teacher-upload-form" onSubmit={handleSubmit}>
          <input
            type="number"
            placeholder="Enter Teacher ID"
            value={teacherId}
            onChange={(e) => setTeacherId(e.target.value)}
            required
          />

          <input
            type="text"
            placeholder="Enter Student Name"
            value={studentName}
            onChange={(e) => setStudentName(e.target.value)}
            required
          />

          <input
            type="file"
            accept=".pdf,.png,.jpg,.jpeg"
            onChange={handleFileChange}
            required
          />

          <button type="submit" className="teacher-upload-btn" disabled={loading}>
            {loading ? "Uploading..." : "Upload"}
          </button>
        </form>

        {message && <p className="teacher-upload-message">{message}</p>}
      </div>
    </div>
  );
};

export default TeacherUploadPage;