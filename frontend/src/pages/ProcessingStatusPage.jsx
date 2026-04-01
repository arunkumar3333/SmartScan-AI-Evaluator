import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProcessingStatus } from "../api/processingApi.js";
import "../styles/ProcessingStatusPage.css";

const ProcessingStatusPage = () => {
  const { answerSheetId } = useParams();
  const navigate = useNavigate();

  const [statusData, setStatusData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchStatus();
  }, [answerSheetId]);

  const fetchStatus = async () => {
    try {
      setLoading(true);
      const data = await getProcessingStatus(answerSheetId);
      setStatusData(data);
    } catch (error) {
      setMessage("Failed to fetch processing status");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="status-page">
      <div className="status-container">
        <div className="status-header">
          <h1>Processing Status</h1>
          <button onClick={() => navigate("/teacher-upload-history")}>Back</button>
        </div>

        {loading ? (
          <div className="status-card">
            <p>Loading processing status...</p>
          </div>
        ) : message ? (
          <div className="status-card error">
            <p>{message}</p>
          </div>
        ) : (
          <div className="status-card">
            <h2>Answer Sheet Details</h2>
            <p><strong>Answer Sheet ID:</strong> {statusData.answerSheetId}</p>
            <p><strong>File Name:</strong> {statusData.fileName}</p>
            <p><strong>Status:</strong> <span className={`status-badge ${statusData.status?.toLowerCase()}`}>{statusData.status}</span></p>

            <div className="status-actions">
              <button onClick={fetchStatus}>Refresh</button>
              <button onClick={() => navigate(`/ocr-result/${answerSheetId}`)}>View OCR</button>
              <button onClick={() => navigate(`/evaluation-result/${answerSheetId}`)}>View Evaluation</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProcessingStatusPage;