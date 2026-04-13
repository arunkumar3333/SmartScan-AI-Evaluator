import axios from "axios";

const API = axios.create({
  baseURL: "https://smartscan-ai-evaluator-1.onrender.com/api", // 🔥 force this
});

export default API;