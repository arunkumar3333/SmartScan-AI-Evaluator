import React, { useEffect, useState } from "react";
import {
  uploadAnswerSheet,
  getUploadsByTeacherId,
  getUploadCountByTeacherId,
  deleteUploadById,
} from "../api/uploadApi";
import { createQuestion, getQuestions } from "../api/questionApi";
import { getStoredUser, logoutUser } from "../utils/auth";
import { useNavigate } from "react-router-dom";
import "../styles/TeacherDashboardPage.css";

const TeacherDashboardPage = () => {
  const navigate = useNavigate();

  const [active, setActive] = useState("overview");

  const [teacherId, setTeacherId] = useState("");
  const [teacherName, setTeacherName] = useState("");

  const [questions, setQuestions] = useState([]);
  const [uploads, setUploads] = useState([]);
  const [uploadCount, setUploadCount] = useState(0);

  const [studentName, setStudentName] = useState("");
  const [questionId, setQuestionId] = useState("");
  const [file, setFile] = useState(null);

  const [title, setTitle] = useState("");
  const [modelAnswer, setModelAnswer] = useState("");

  const [message, setMessage] = useState("");

  useEffect(() => {
    const user = getStoredUser();
    if (!user) return navigate("/login");

    setTeacherId(user.id);
    setTeacherName(user.name || "Teacher");

    fetchData(user.id);
    fetchQuestions();
  }, []);

  const fetchData = async (id) => {
    setUploads(await getUploadsByTeacherId(id));
    setUploadCount(await getUploadCountByTeacherId(id));
  };

  const fetchQuestions = async () => {
    setQuestions(await getQuestions());
  };

  const handleLogout = () => {
    logoutUser();
    navigate("/login");
  };

  const handleCreateModel = async (e) => {
    e.preventDefault();
    if (!title || !modelAnswer) return setMessage("Fill all fields");

    await createQuestion({ title, modelAnswer, teacherId });

    setTitle("");
    setModelAnswer("");
    setMessage("Model created successfully");
    fetchQuestions();
  };

  const handleUpload = async (e) => {
    e.preventDefault();

    if (!studentName || !file || !questionId)
      return setMessage("Fill all fields");

    const formData = new FormData();
    formData.append("teacherId", teacherId);
    formData.append("studentName", studentName);
    formData.append("file", file);
    formData.append("questionId", questionId);

    const res = await uploadAnswerSheet(formData);

    setStudentName("");
    setFile(null);
    setQuestionId("");
    setMessage("Upload successful");

    fetchData(teacherId);

    if (res?.id) navigate(`/processing-status/${res.id}`);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this upload?")) return;
    await deleteUploadById(id);
    fetchData(teacherId);
  };

  return (
    <div className="layout">

      {/* SIDEBAR */}
      <div className="sidebar">
        <h2>SmartScan</h2>

        <button className={active==="overview"?"active":""} onClick={()=>setActive("overview")}>Overview</button>
        <button className={active==="model"?"active":""} onClick={()=>setActive("model")}>Create Model</button>
        <button className={active==="upload"?"active":""} onClick={()=>setActive("upload")}>Upload</button>
        <button className={active==="sheets"?"active":""} onClick={()=>setActive("sheets")}>Uploaded Sheets</button>

        <button className="logout" onClick={handleLogout}>Logout</button>
      </div>

      {/* CONTENT */}
      <div className="content">
        <div className="content-inner">

          <div className="header">
            <h1>Teacher Dashboard</h1>
            <p>Welcome, {teacherName}</p>
          </div>

          {message && <p className="message">{message}</p>}

          {/* OVERVIEW */}
          {active === "overview" && (
            <div className="card">
              <h3>SmartScan AI Evaluation</h3>
              <p className="desc">
                Evaluate answer sheets automatically using AI. Upload student sheets,
                extract text (OCR), compare with model answers, and generate scores
                with feedback instantly.
              </p>

              <div className="stat-box">
                <p>Total Uploads</p>
                <h2>{uploadCount}</h2>
              </div>
            </div>
          )}

          {/* CREATE MODEL */}
          {active === "model" && (
            <div className="card">
              <h2>Create Model Answer</h2>

              <form className="form-container" onSubmit={handleCreateModel}>
                <input placeholder="Model Name" value={title} onChange={(e)=>setTitle(e.target.value)} />
                <textarea className="model-answer" placeholder="Model Answer" value={modelAnswer} onChange={(e)=>setModelAnswer(e.target.value)} />
                <button className="primary-btn">Create</button>
              </form>
            </div>
          )}

          {/* UPLOAD */}
          {active === "upload" && (
            <div className="card">
              <h2>Upload Answer Sheet</h2>

              <form className="form-container" onSubmit={handleUpload}>
                <input value={`Teacher ID: ${teacherId}`} readOnly />

                <input
                  placeholder="Student Name"
                  value={studentName}
                  onChange={(e)=>setStudentName(e.target.value)}
                />

                <select value={questionId} onChange={(e)=>setQuestionId(e.target.value)}>
                  <option value="">Select Model</option>
                  {questions.map((q)=>(
                    <option key={q.id} value={q.id}>{q.title}</option>
                  ))}
                </select>

                <input type="file" onChange={(e)=>setFile(e.target.files[0])} />

                <button className="primary-btn">Upload</button>
              </form>
            </div>
          )}

          {/* TABLE */}
          {active === "sheets" && (
            <div className="card">
              <h2>Uploaded Sheets</h2>

              <table className="upload-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Student Name</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {uploads.map((u)=>(
                    <tr key={u.id}>
                      <td className="col-id">{u.id}</td>
                      <td className="col-student">{u.studentName}</td>
                      <td className="col-status">
                        <span className="status">{u.status}</span>
                      </td>
                      <td className="col-actions">
                        <div className="action-group">
                          <button onClick={()=>navigate(`/processing-status/${u.id}`)}>Status</button>
                          <button onClick={()=>navigate(`/ocr-result/${u.id}`)}>OCR</button>
                          <button onClick={()=>navigate(`/evaluation-result/${u.id}`)}>Eval</button>
                          <button className="danger" onClick={()=>handleDelete(u.id)}>Delete</button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>

            </div>
          )}

        </div>
      </div>
    </div>
  );
};

export default TeacherDashboardPage;