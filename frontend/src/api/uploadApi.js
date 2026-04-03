import axios from "axios";

const UPLOAD_API = "http://localhost:8081/api/upload";

export const uploadAnswerSheet = async (formData) => {
  const response = await axios.post(`${UPLOAD_API}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};

export const getAllUploads = async () => {
  const response = await axios.get(`${UPLOAD_API}/all`);
  return response.data;
};

export const getUploadsByTeacherId = async (teacherId) => {
  const response = await axios.get(`${UPLOAD_API}/teacher/${teacherId}`);
  return response.data;
};

export const getUploadCountByTeacherId = async (teacherId) => {
  const response = await axios.get(`${UPLOAD_API}/teacher/${teacherId}/count`);
  return response.data;
};

export const getUploadById = async (id) => {
  const response = await axios.get(`${UPLOAD_API}/id/${id}`);
  return response.data;
};

export const deleteUploadById = async (id) => {
  const response = await axios.delete(`${UPLOAD_API}/id/${id}`);
  return response.data;
};
