import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getEvaluationResult } from "../api/processingApi.js";
 import "../styles/EvaluationResultPage.css";

const EvaluationResultPage = () => {
  const { answerSheetId } = useParams();
  const navigate = useNavigate();

  const [evaluationData, setEvaluationData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchEvaluationResult();
  }, [answerSheetId]);

  const fetchEvaluationResult = async () => {
    try {
      setLoading(true);
      const data = await getEvaluationResult(answerSheetId);
      setEvaluationData(data);
    } catch (error) {
      setMessage("Failed to fetch evaluation result");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="evaluation-page">
      <div className="evaluation-container">
        <div className="evaluation-header">
          <h1>Evaluation Result</h1>
          <button onClick={() => navigate("/teacher-dashboard")}>Back</button>
        </div>

        {loading ? (
          <div className="evaluation-card">
            <p>Loading evaluation result...</p>
          </div>
        ) : message ? (
          <div className="evaluation-card error">
            <p>{message}</p>
          </div>
        ) : (
          <div className="evaluation-card">
            <p><strong>Answer Sheet ID:</strong> {evaluationData.answerSheetId}</p>
            <p><strong>File Name:</strong> {evaluationData.fileName}</p>
            <p><strong>Status:</strong> {evaluationData.status}</p>

            <div className="score-box">
              <h2>Score</h2>
              <p>{evaluationData.score}</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default EvaluationResultPage;