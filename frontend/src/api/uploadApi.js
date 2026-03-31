// src/api/uploadApi.js

import axios from "axios";

// Base URL for upload APIs
const UPLOAD_API = "http://localhost:8080/api/upload";

// Upload a new answer sheet
export const uploadAnswerSheet = async (formData) => {
  const response = await axios.post(UPLOAD_API, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};

// Get all uploads (mainly for admin)
export const getAllUploads = async () => {
  const response = await axios.get(UPLOAD_API);
  return response.data;
};

// Get uploads for one teacher
export const getUploadsByTeacherId = async (teacherId) => {
  const response = await axios.get(`${UPLOAD_API}/teacher/${teacherId}`);
  return response.data;
};

// Get upload count for one teacher
export const getUploadCountByTeacherId = async (teacherId) => {
  const response = await axios.get(`${UPLOAD_API}/teacher/${teacherId}/count`);
  return response.data;
};

// Delete one upload by id
export const deleteUploadById = async (id) => {
  const response = await axios.delete(`${UPLOAD_API}/${id}`);
  return response.data;
};