import axios from "axios";
const ADMIN_API = `${import.meta.env.VITE_API_URL}/api/admin`;
//const ADMIN_API = "http://localhost:8080/api/admin";

// Get admin dashboard stats
export const getAdminDashboardStats = async () => {
  const response = await axios.get(`${ADMIN_API}/dashboard`);
  return response.data;
};

// Get all teachers
export const getAllTeachers = async () => {
  const response = await axios.get(`${ADMIN_API}/teachers`);
  return response.data;
};

// Get pending teachers
export const getPendingTeachers = async () => {
  const response = await axios.get(`${ADMIN_API}/teachers/pending`);
  return response.data;
};

// Approve teacher
export const approveTeacher = async (teacherId) => {
  const response = await axios.put(`${ADMIN_API}/teachers/${teacherId}/approve`);
  return response.data;
};

// Disapprove teacher
export const disapproveTeacher = async (teacherId) => {
  const response = await axios.put(`${ADMIN_API}/teachers/${teacherId}/disapprove`);
  return response.data;
};

// Remove teacher
export const removeTeacher = async (teacherId) => {
  const response = await axios.delete(`${ADMIN_API}/teachers/${teacherId}`);
  return response.data;
};