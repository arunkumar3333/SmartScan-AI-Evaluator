import React from "react";
import { Navigate } from "react-router-dom";
import { getStoredUser, getToken } from "../utils/auth";

const ProtectedRoute = ({ children, allowedRole }) => {
  const user = getStoredUser();
  const token = getToken();

  if (!user || !token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRole && user.role !== allowedRole) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;