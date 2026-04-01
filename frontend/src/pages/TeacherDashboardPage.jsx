import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  uploadAnswerSheet,
  getUploadsByTeacherId,
  getUploadCountByTeacherId,
  deleteUploadById,
} from "../api/uploadApi";
import { getStoredUser, logoutUser } from "../utils/auth";
import "../styles/TeacherDashboardPage.css";

const TeacherDashboardPage = () => {
  const navigate = useNavigate();

  const [teacherId, setTeacherId] = useState("");
  const [teacherName, setTeacherName] = useState("");

  const [studentName, setStudentName] = useState("");
  const [file, setFile] = useState(null);

  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  const [uploads, setUploads] = useState([]);
  const [uploadCount, setUploadCount] = useState(0);

  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);

  useEffect(() => {
    const user = getStoredUser();

    if (!user) {
      navigate("/login");
      return;
    }

    setTeacherId(user.id);
    setTeacherName(user.name || "Teacher");

    fetchTeacherData(user.id);
  }, [navigate]);

  const fetchTeacherData = async (id) => {
    try {
      setPageLoading(true);

      const uploadsData = await getUploadsByTeacherId(id);
      const countData = await getUploadCountByTeacherId(id);

      setUploads(uploadsData || []);
      setUploadCount(countData || 0);
    } catch (error) {
      setMessage("Failed to load dashboard data");
      setMessageType("error");
    } finally {
      setPageLoading(false);
    }
  };

  const formatDateTime = (dateTime) => {
    if (!dateTime) return "N/A";

    const date = new Date(dateTime);

    return date.toLocaleString("en-IN", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      hour12: true,
    });
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setMessage("");
    setMessageType("");
  };

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setMessage("");
    setMessageType("");

    if (!studentName || !file) {
      setMessage("Please enter student name and choose a file.");
      setMessageType("error");
      return;
    }

    const formData = new FormData();
    formData.append("teacherId", teacherId);
    formData.append("studentName", studentName);
    formData.append("files", file);

    try {
      setLoading(true);

      const response = await uploadAnswerSheet(formData);
      const data = response[0];

      setMessage(`Upload successful: ${response.fileName || "File uploaded"}`);
      setMessageType("success");
      
      setStudentName("");
      setFile(null);
      e.target.reset();

      await fetchTeacherData(teacherId);

      if (response?.id) {
        navigate(`/processing-status/${response.id}`);
      }
    } catch (error) {
      setMessage(error.response?.data?.error || "Upload failed");
      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this upload?");
    if (!confirmDelete) return;

    try {
      await deleteUploadById(id);

      setMessage("Upload deleted successfully");
      setMessageType("success");

      fetchTeacherData(teacherId);
    } catch (error) {
      setMessage(error.response?.data?.error || "Delete failed");
      setMessageType("error");
    }
  };

  return (
    <div className="teacher-dashboard-page">
      <div className="teacher-dashboard-container">
        <div className="teacher-dashboard-header">
          <div>
            <h1>Teacher Dashboard</h1>
            <p>Welcome, {teacherName}</p>
          </div>

          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <div className="dashboard-stats">
          <div className="stat-card">
            <h3>Total Uploads</h3>
            <p>{uploadCount}</p>
          </div>
        </div>

        <div className="dashboard-card">
          <h2>Upload Answer Sheet</h2>

          <form className="teacher-upload-form" onSubmit={handleSubmit}>
            <input type="text" value={`Teacher ID: ${teacherId}`} readOnly />

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

            {file && <p className="file-name">Selected file: {file.name}</p>}

            <div className="teacher-form-actions">
              <button type="submit" disabled={loading}>
                {loading ? "Uploading..." : "Upload"}
              </button>

              <button
                type="button"
                className="secondary-btn"
                onClick={() => navigate("/teacher-upload-history")}
              >
                View Upload History
              </button>
            </div>
          </form>

          {message && (
            <p className={`dashboard-message ${messageType}`}>{message}</p>
          )}
        </div>

        <div className="dashboard-card">
          <h2>Uploaded Sheets</h2>

          {pageLoading ? (
            <p>Loading uploads...</p>
          ) : uploads.length === 0 ? (
            <p>No uploads found.</p>
          ) : (
            <div className="upload-table-wrapper">
              <table className="upload-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Student Name</th>
                    <th>File Name</th>
                    <th>Status</th>
                    <th>Upload Time</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {uploads.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>{item.studentName}</td>
                      <td>{item.fileName}</td>
                      <td>{item.status}</td>
                      <td>{formatDateTime(item.uploadTime)}</td>
                      <td className="table-actions">
                        <button
                          type="button"
                          className="view-btn"
                          onClick={() => navigate(`/processing-status/${item.id}`)}
                        >
                          Status
                        </button>

                        <button
                          type="button"
                          className="view-btn"
                          onClick={() => navigate(`/ocr-result/${item.id}`)}
                        >
                          OCR
                        </button>

                        <button
                          type="button"
                          className="view-btn"
                          onClick={() => navigate(`/evaluation-result/${item.id}`)}
                        >
                          Evaluation
                        </button>

                        <button
                          type="button"
                          className="delete-btn"
                          onClick={() => handleDelete(item.id)}
                        >
                          Delete
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
    </div>
  );
};

export default TeacherDashboardPage;