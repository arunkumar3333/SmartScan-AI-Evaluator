import axios from "axios";

const PROCESS_API = "http://localhost:8080/api/process";
const OCR_API = "http://localhost:8080/api/ocr";
const EVALUATION_API = "http://localhost:8080/api/evaluation";

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