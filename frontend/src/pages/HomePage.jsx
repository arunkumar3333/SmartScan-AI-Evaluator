import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/HomePage.css";

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="home-page">
      <div className="home-card">
        <h1 className="home-title">SmartScan AI Evaluator</h1>

        <p className="home-description">
          Upload answer sheets, extract text using OCR, and evaluate automatically.
        </p>

        <div className="home-buttons">
          <button
            className="home-btn login-btn"
            onClick={() => navigate("/login")}
          >
            Login
          </button>

          <button
            className="home-btn register-btn"
            onClick={() => navigate("/register")}
          >
            Register
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomePage;