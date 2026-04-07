import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getUploadsByTeacherId } from "../api/uploadApi";
import { getStoredUser } from "../utils/auth";
import "../styles/TeacherUploadHistoryPage.css";

const TeacherUploadHistoryPage = () => {
  const navigate = useNavigate();
  const [uploads, setUploads] = useState([]);
  const [teacherName, setTeacherName] = useState("");
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const user = getStoredUser();

    if (!user) {
      navigate("/login");
      return;
    }

    setTeacherName(user.name || "Teacher");
    fetchUploads(user.id);
  }, [navigate]);

  const fetchUploads = async (teacherId) => {
    try {
      setLoading(true);
      const data = await getUploadsByTeacherId(teacherId);
      setUploads(data);
    } catch (error) {
      setMessage("Failed to fetch upload history");
    } finally {
      setLoading(false);
    }
  };

  const formatDateTime = (dateTime) => {
    if (!dateTime) return "N/A";

    return new Date(dateTime).toLocaleString("en-IN", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      hour12: true,
    });
  };

  return (
    <div className="teacher-history-page">
      <div className="teacher-history-container">
        <div className="teacher-history-header">
          <div>
            <h1>Teacher Upload History</h1>
            <p>Welcome, {teacherName}</p>
          </div>
          <button className="back-btn" onClick={() => navigate("/teacher-dashboard")}>
            Back
          </button>
          
        </div>

        {message && <p className="history-message">{message}</p>}

        {loading ? (
          <p className="history-loading">Loading upload history...</p>
        ) : uploads.length === 0 ? (
          <p className="history-empty">No uploads found.</p>
        ) : (
          <div className="history-table-wrapper">
            <table className="history-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Student Name</th>
                  <th>File Name</th>
                  <th>Status</th>
                  <th>Score</th>
                  <th>Upload Time</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {uploads.map((item) => (
                  <tr key={item.id}>
                    <td>{item.id}</td>
                    <td>{item.studentName}</td>
                    <td>{item.fileName}</td>
                    <td>{item.status}</td>
                    <td>{item.score ?? "N/A"}</td>
                    <td>{formatDateTime(item.uploadTime)}</td>
                    <td className="history-actions">
                      <button onClick={() => navigate(`/processing-status/${item.id}`)}>
                        Status
                      </button>
                      <button onClick={() => navigate(`/ocr-result/${item.id}`)}>
                        OCR
                      </button>
                      <button onClick={() => navigate(`/evaluation-result/${item.id}`)}>
                        Evaluation
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default TeacherUploadHistoryPage;