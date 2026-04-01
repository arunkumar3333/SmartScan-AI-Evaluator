import React from "react";
import { Link } from "react-router-dom";
import "../styles/NotFoundPage.css";

// 404 page for invalid routes
const NotFoundPage = () => {
  return (
    <div className="notfound-page">
      <div className="notfound-card">
        <h1>404</h1>
        <p>Page not found</p>
        <Link to="/">Go Home</Link>
      </div>
    </div>
  );
};

export default NotFoundPage;