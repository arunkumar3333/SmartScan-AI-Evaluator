import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllUploads } from "../api/uploadApi";
import { getAdminDashboardStats } from "../api/adminApi";
import { getStoredUser, logoutUser } from "../utils/auth";
import "../styles/AdminDashboardPage.css";

const AdminDashboardPage = () => {
  const navigate = useNavigate();

  const [adminName, setAdminName] = useState("");
  const [uploads, setUploads] = useState([]);
  const [stats, setStats] = useState({
    totalUploads: 0,
    totalTeachers: 0,
  });
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

  const fetchAdminData = async () => {
    try {
      setLoading(true);

      const uploadsData = await getAllUploads();
      const statsData = await getAdminDashboardStats();

      setUploads(uploadsData);
      setStats(statsData);
    } catch (error) {
      console.error("Failed to fetch admin data", error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
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

        <div className="admin-card">
          <h2>All Uploaded Sheets</h2>

          {loading ? (
            <p>Loading data...</p>
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
                  </tr>
                </thead>
                <tbody>
                  {uploads.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>{item.studentName}</td>
                      <td>{item.fileName}</td>
                      <td>{item.status}</td>
                      <td>{item.uploadTime}</td>
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