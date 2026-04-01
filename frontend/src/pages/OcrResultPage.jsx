import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getOcrResult } from "../api/processingApi.js";
import "../styles/OcrResultPage.css";

const OcrResultPage = () => {
  const { answerSheetId } = useParams();
  const navigate = useNavigate();

  const [ocrData, setOcrData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchOcrResult();
  }, [answerSheetId]);

  const fetchOcrResult = async () => {
    try {
      setLoading(true);
      const data = await getOcrResult(answerSheetId);
      setOcrData(data);
    } catch (error) {
      setMessage("Failed to fetch OCR result");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ocr-page">
      <div className="ocr-container">
        <div className="ocr-header">
          <h1>OCR Result</h1>
          <button onClick={() => navigate("/teacher-upload-history")}>Back</button>
        </div>

        {loading ? (
          <div className="ocr-card">
            <p>Loading OCR result...</p>
          </div>
        ) : message ? (
          <div className="ocr-card error">
            <p>{message}</p>
          </div>
        ) : (
          <div className="ocr-card">
            <p><strong>Answer Sheet ID:</strong> {ocrData.answerSheetId}</p>
            <h2>Extracted Text</h2>
            <div className="ocr-text-box">
              {ocrData.extractedText?.trim() ? ocrData.extractedText : "No extracted text available."}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default OcrResultPage;