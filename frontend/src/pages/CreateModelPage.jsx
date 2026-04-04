import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createQuestion } from "../api/questionApi";
import { getStoredUser } from "../utils/auth";

import "../styles/CreateModelPage.css";

const CreateModelPage = () => {
  const navigate = useNavigate();

  const user = getStoredUser();

  const [title, setTitle] = useState("");
  const [questionText, setQuestionText] = useState("");
  const [modelAnswer, setModelAnswer] = useState("");

  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    setMessage("");
    setMessageType("");

    if (!title.trim() || !modelAnswer.trim()) {
      setMessage("Model name and answer are required");
      setMessageType("error");
      return;
    }

    try {
      setLoading(true);

      await createQuestion({
        title: title.trim(), // ✅ IMPORTANT
        questionText: questionText.trim(),
        modelAnswer: modelAnswer.trim(),
        teacherId: user?.id, // ✅ dynamic
      });

      setMessage("Model created successfully ✅");
      setMessageType("success");

      // reset
      setTitle("");
      setQuestionText("");
      setModelAnswer("");

      // optional: auto redirect after 1.5 sec
      setTimeout(() => {
        navigate("/teacher-dashboard");
      }, 1500);

    } catch (error) {
      console.error(error);
      setMessage("Failed to create model ❌");
      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="create-model-page">
      <div className="create-model-container">

        <h2>Create Model Answer</h2>

        <form onSubmit={handleSubmit} className="create-model-form">

          <input
            type="text"
            placeholder="Model Name (e.g Science)"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />

          <textarea
            placeholder="Question (optional)"
            value={questionText}
            onChange={(e) => setQuestionText(e.target.value)}
          />

          <textarea
            placeholder="Model Answer"
            value={modelAnswer}
            onChange={(e) => setModelAnswer(e.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? "Creating..." : "Create Model"}
          </button>

        </form>

        {message && (
          <p className={`message ${messageType}`}>
            {message}
          </p>
        )}

        <button
          className="back-btn"
          onClick={() => navigate("/teacher-dashboard")}
        >
          ← Back to Dashboard
        </button>

      </div>
    </div>
  );
};

export default CreateModelPage;