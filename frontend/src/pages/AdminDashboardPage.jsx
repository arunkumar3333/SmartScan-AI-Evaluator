import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllUploads, deleteUploadById } from "../api/uploadApi";
import {
  getAdminDashboardStats,
  getAllTeachers,
  approveTeacher,
  disapproveTeacher,
  removeTeacher,
} from "../api/adminApi";
import { getStoredUser, logoutUser } from "../utils/auth";
import "../styles/AdminDashboardPage.css";

const AdminDashboardPage = () => {
  const navigate = useNavigate();

  // Logged in admin name
  const [adminName, setAdminName] = useState("");

  // Upload list
  const [uploads, setUploads] = useState([]);

  // Teacher list
  const [teachers, setTeachers] = useState([]);

  // Dashboard stats
  const [stats, setStats] = useState({
    totalUploads: 0,
    totalTeachers: 0,
  });

  // UI message
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  // Loading state
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const user = getStoredUser();

    if (!user) {
      navigate("/login");
      return;
    }

    setAdminName(user.name || "Admin");
    fetchAdminData();
  }, [navigate]);

  // Load admin data
  const fetchAdminData = async () => {
    try {
      setLoading(true);

      const uploadsData = await getAllUploads();
      const statsData = await getAdminDashboardStats();
      const teachersData = await getAllTeachers();

      setUploads(uploadsData);
      setStats(statsData);
      setTeachers(teachersData);
    } catch (error) {
      setMessage("Failed to fetch admin data");
      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  // Format upload time
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

  // Logout
  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  // Delete upload
  const handleDeleteUpload = async (id) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this upload?");
    if (!confirmDelete) return;

    try {
      await deleteUploadById(id);
      setMessage("Upload deleted successfully");
      setMessageType("success");
      fetchAdminData();
    } catch (error) {
      setMessage("Failed to delete upload");
      setMessageType("error");
    }
  };

  // Approve teacher
  const handleApproveTeacher = async (teacherId) => {
    try {
      await approveTeacher(teacherId);
      setMessage("Teacher approved successfully");
      setMessageType("success");
      fetchAdminData();
    } catch (error) {
      setMessage("Failed to approve teacher");
      setMessageType("error");
    }
  };

  // Disapprove teacher
  const handleDisapproveTeacher = async (teacherId) => {
    try {
      await disapproveTeacher(teacherId);
      setMessage("Teacher disapproved successfully");
      setMessageType("success");
      fetchAdminData();
    } catch (error) {
      setMessage("Failed to disapprove teacher");
      setMessageType("error");
    }
  };

  // Remove teacher
  const handleRemoveTeacher = async (teacherId) => {
    const confirmDelete = window.confirm("Are you sure you want to remove this teacher?");
    if (!confirmDelete) return;

    try {
      await removeTeacher(teacherId);
      setMessage("Teacher removed successfully");
      setMessageType("success");
      fetchAdminData();
    } catch (error) {
      setMessage("Failed to remove teacher");
      setMessageType("error");
    }
  };

  return (
    <div className="admin-dashboard-page">
      <div className="admin-dashboard-container">
        <div className="admin-dashboard-header">
          <div>
            <h1>Admin Dashboard</h1>
            <p>Welcome, {adminName}</p>
          </div>

          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <div className="admin-stats">
          <div className="admin-stat-card">
            <h3>Total Uploads</h3>
            <p>{stats.totalUploads}</p>
          </div>

          <div className="admin-stat-card">
            <h3>Total Teachers</h3>
            <p>{stats.totalTeachers}</p>
          </div>
        </div>

        {message && <p className={`admin-message ${messageType}`}>{message}</p>}

        <div className="admin-card">
          <h2>Teacher Management</h2>

          {loading ? (
            <p>Loading teachers...</p>
          ) : teachers.length === 0 ? (
            <p>No teachers found.</p>
          ) : (
            <div className="admin-table-wrapper">
              <table className="admin-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Approved</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {teachers.map((teacher) => (
                    <tr key={teacher.id}>
                      <td>{teacher.id}</td>
                      <td>{teacher.name}</td>
                      <td>{teacher.email}</td>
                      <td>{teacher.approved ? "Yes" : "Pending"}</td>
                      <td>
                        {!teacher.approved ? (
                          <button
                            className="approve-btn"
                            onClick={() => handleApproveTeacher(teacher.id)}
                          >
                            Approve
                          </button>
                        ) : (
                          <button
                            className="disapprove-btn"
                            onClick={() => handleDisapproveTeacher(teacher.id)}
                          >
                            Disapprove
                          </button>
                        )}

                        <button
                          className="delete-btn"
                          onClick={() => handleRemoveTeacher(teacher.id)}
                        >
                          Remove
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="admin-card">
          <h2>All Uploaded Sheets</h2>

          {loading ? (
            <p>Loading uploads...</p>
          ) : uploads.length === 0 ? (
            <p>No uploads found.</p>
          ) : (
            <div className="admin-table-wrapper">
              <table className="admin-table">
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
                      <td>
                        <button
                          className="delete-btn"
                          onClick={() => handleDeleteUpload(item.id)}
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

export default AdminDashboardPage;