import axios from "axios";

const ADMIN_API = "http://localhost:8080/api/admin";

export const getAdminDashboardStats = async () => {
  const response = await axios.get(`${ADMIN_API}/dashboard`);
  return response.data;
};