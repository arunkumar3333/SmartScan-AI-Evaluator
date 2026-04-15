import axios from "axios";

// const PROCESS_API = "http://localhost:8080/api/process";
// const UPLOAD_API = "http://localhost:8080/api/upload";
const BASE_URL = import.meta.env.VITE_API_URL;

const PROCESS_API = `${BASE_URL}/api/process`;
const UPLOAD_API = `${BASE_URL}/api/upload`;
//Upload (PROCESS)
export const uploadAnswerSheet = async (formData) => {
                        //sends data to backend
  const response = await axios.post(`${PROCESS_API}/upload`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",//Tells backend:“I am sending file + data”
    },
  });
  return response.data;
};
export const getAllUploads = async () => {
  const response = await axios.get(`${UPLOAD_API}/all`)
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