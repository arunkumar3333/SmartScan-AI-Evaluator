import API from "./api";

// ✅ create model
export const createQuestion = async (data) => {
  const res = await API.post("/questions", data);
  return res.data;
};

// ✅ get all models
export const getQuestions = async () => {
  const res = await API.get("/questions");
  return res.data;
};