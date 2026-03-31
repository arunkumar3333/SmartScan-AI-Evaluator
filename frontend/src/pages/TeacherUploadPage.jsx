import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { uploadAnswerSheet } from "../api/uploadApi";
import "../styles/TeacherUploadPage.css";

const TeacherUploadPage = () => {
  const navigate = useNavigate();

  const [teacherId, setTeacherId] = useState("");
  const [studentName, setStudentName] = useState("");
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (!storedUser) {
      navigate("/login");
      return;
    }

    const user = JSON.parse(storedUser);

    if (user?.id) {
      setTeacherId(user.id);
    } else {
      setMessage("Teacher ID not found. Please login again.");
      setMessageType("error");
    }
  }, [navigate]);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setMessage("");
    setMessageType("");
  };

  const handleLogout = () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setMessage("");
    setMessageType("");

    if (!teacherId || !studentName || !file) {
      setMessage("Please fill all fields and choose a file.");
      setMessageType("error");
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
      setMessageType("success");

      setStudentName("");
      setFile(null);
      e.target.reset();
    } catch (error) {
      setMessage(error.response?.data?.error || "Upload failed");
      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="teacher-upload-page">
      <div className="teacher-upload-card">
        <div className="teacher-upload-topbar">
          <h2 className="teacher-upload-title">Upload Answer Sheet</h2>
          <button type="button" className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <p className="teacher-upload-subtitle">
          Upload scanned PDF, JPG, JPEG, or PNG files
        </p>

        <form className="teacher-upload-form" onSubmit={handleSubmit}>
          <input
            type="text"
            value={`Teacher ID: ${teacherId}`}
            readOnly
          />

          <input
            type="text"
            placeholder="Enter Student Name"
            value={studentName}
            onChange={(e) => {
              setStudentName(e.target.value);
              setMessage("");
              setMessageType("");
            }}
            required
          />

          <input
            type="file"
            accept=".pdf,.png,.jpg,.jpeg"
            onChange={handleFileChange}
            required
          />

          {file && <p className="file-name">Selected file: {file.name}</p>}

          <button
            type="submit"
            className="teacher-upload-btn"
            disabled={loading}
          >
            {loading ? "Uploading..." : "Upload"}
          </button>
        </form>

        {message && (
          <p className={`teacher-upload-message ${messageType}`}>
            {message}
          </p>
        )}
      </div>
    </div>
  );
};

export default TeacherUploadPage;