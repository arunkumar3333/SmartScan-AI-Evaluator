import axios from "axios";

<<<<<<< HEAD
const PROCESS_API = "http://localhost:8081/api/process";
const UPLOAD_API = "http://localhost:8081/api/upload";
=======
const PROCESS_API = "http://localhost:8080/api/process";
const UPLOAD_API = "http://localhost:8080/api/upload";
>>>>>>> dev1

// ✅ Upload (PROCESS)
export const uploadAnswerSheet = async (formData) => {
  const response = await axios.post(`${PROCESS_API}/upload`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};
export const getAllUploads = async () => {
  const response = await axios.get("http://localhost:8081/api/upload/all");
  return response.data;
};
// Dashboard (UPLOAD)
export const getUploadsByTeacherId = async (teacherId) => {
  const response = await axios.get(`${UPLOAD_API}/teacher/${teacherId}`);
  return response.data;
};

export const getUploadCountByTeacherId = async (teacherId) => {
  const response = await axios.get(`${UPLOAD_API}/teacher/${teacherId}/count`);
  return response.data;
};

export const deleteUploadById = async (id) => {
  const response = await axios.delete(`${UPLOAD_API}/id/${id}`);
  return response.data;
};