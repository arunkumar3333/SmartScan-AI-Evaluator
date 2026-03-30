import axios from "axios";

const UPLOAD_API = "http://localhost:8080/api/upload";

export const uploadAnswerSheet = async (formData) => {
  const response = await axios.post(UPLOAD_API, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  return response.data;
};