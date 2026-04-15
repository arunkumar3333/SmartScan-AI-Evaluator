import axios from "axios";

// const PROCESS_API = "http://localhost:8080/api/process";
// const OCR_API = "http://localhost:8080/api/ocr";
// const EVALUATION_API = "http://localhost:8080/api/evaluation";
const BASE_URL = import.meta.env.VITE_API_URL;

const PROCESS_API = `${BASE_URL}/api/process`;
const OCR_API = `${BASE_URL}/api/ocr`;
const EVALUATION_API = `${BASE_URL}/api/evaluation`;
export const getProcessingStatus = async (answerSheetId) => {
  const response = await axios.get(`${PROCESS_API}/status/${answerSheetId}`);
  return response.data;
};

export const getOcrResult = async (answerSheetId) => {
  const response = await axios.get(`${OCR_API}/${answerSheetId}`);
  return response.data;
};

export const getEvaluationResult = async (answerSheetId) => {
  const response = await axios.get(`${EVALUATION_API}/${answerSheetId}`);
  return response.data;
};