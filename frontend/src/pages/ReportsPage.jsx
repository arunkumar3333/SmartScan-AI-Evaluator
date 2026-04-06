import React, { useEffect, useState } from "react";
import { getUploadsByTeacherId } from "../api/uploadApi";
import { getStoredUser } from "../utils/auth";

import "../styles/ReportsPage.css";

const ReportsPage = () => {
  const [uploads, setUploads] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const user = getStoredUser();

    if (user && user.id) {
      fetchData(user.id);
    } else {
      console.error("User not found");
      setLoading(false);
    }
  }, []);

  const fetchData = async (id) => {
    try {
      const response = await getUploadsByTeacherId(id);

      console.log("API RESPONSE:", response);

      // ✅ VERY IMPORTANT FIX
      if (Array.isArray(response)) {
        setUploads(response);
      } else if (Array.isArray(response.data)) {
        setUploads(response.data);
      } else {
        setUploads([]);
      }
    } catch (error) {
      console.error("Fetch error:", error);
      setUploads([]);
    } finally {
      setLoading(false);
    }
  };

  // ✅ LOADING STATE
  if (loading) {
    return <div className="report-page">Loading Reports...</div>;
  }

  // ✅ CALCULATIONS (SAFE)
  const total = uploads.length;
  const processed = uploads.filter((u) => u?.status === "PROCESSED").length;
  const pending = total - processed;

  return (
    <div className="report-page">
      <h1>Reports Dashboard</h1>

      {/* ✅ STATS */}
      <div className="stats">
        <div className="stat-box">
          <h3>Total Uploads</h3>
          <h2>{total}</h2>
        </div>

        <div className="stat-box">
          <h3>Processed</h3>
          <h2>{processed}</h2>
        </div>

        <div className="stat-box">
          <h3>Pending</h3>
          <h2>{pending}</h2>
        </div>
      </div>

      {/* ✅ SAFE TABLE INSTEAD OF CHART (NO CRASH) */}
      <div className="report-table">
        <h3>Uploads List</h3>

        {uploads.length > 0 ? (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Student</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {uploads.map((u) => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.studentName}</td>
                  <td>{u.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>No uploads found</p>
        )}
      </div>
    </div>
  );
};

export default ReportsPage;
