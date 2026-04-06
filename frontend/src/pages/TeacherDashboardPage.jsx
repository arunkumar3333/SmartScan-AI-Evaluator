import React, { useEffect, useState } from "react";
import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
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

import {
  BarChart, Bar, XAxis, YAxis, Tooltip,
  ResponsiveContainer, PieChart, Pie, Cell, Legend
} from "recharts";

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

  useEffect(() => {
    if (!teacherId) return;

    const interval = setInterval(() => {
      fetchData(teacherId);
    }, 3000);

    return () => clearInterval(interval);
  }, [teacherId]);
  useEffect(() => {
  console.log("uploads:", uploads);
  console.log("questions:", questions);
}, [uploads, questions]);

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

  // ✅ MODEL-WISE ANALYSIS DATA
// ✅ MODEL-WISE ANALYSIS DATA (USE THIS)
const modelWiseData = Object.values(
  uploads.reduce((acc, u) => {

    const modelName = u.modelName || "Unknown";

    if (!acc[modelName]) {
      acc[modelName] = {
        questionId: u.questionId,
        model: modelName,
        total: 0,
        totalScore: 0,
      };
    }

    acc[modelName].total += 1;
    acc[modelName].totalScore += (u.score || 0);

    return acc;
  }, {})
).map(m => ({
  ...m,
  avgScore: (m.totalScore / m.total).toFixed(1),
}));

// ✅ EXPORT CSV
const exportCSV = () => {
  const rows = [
    ["ID", "Name", "Score", "AI Score", "Similarity"],
    ...uploads.map(u => [
      u.id,
      u.studentName,
      u.score ?? "N/A",
      u.llmScore ?? "N/A",
      u.similarity ?? "N/A"
    ])
  ];

  const csv =
    "data:text/csv;charset=utf-8," +
    rows.map(r => r.join(",")).join("\n");

  const link = document.createElement("a");
  link.href = encodeURI(csv);
  link.download = "results.csv";
  link.click();
};

//export pdf
const exportPDF = () => {
  const doc = new jsPDF();

  const tableData = uploads.map(u => [
    u.id,
    u.studentName,
    u.score ?? "N/A",
    u.llmScore ?? "N/A",
    u.similarity ?? "N/A"
  ]);

  autoTable(doc, {
    head: [["ID", "Name", "Score", "AI", "Similarity"]],
    body: tableData,
  });

  doc.save("report.pdf");
};

//EXPORT CSV/PDF FOR PARTICULAR QUESTION (MODEL)
const exportModelCSV = (questionId) => {
  const filtered = uploads.filter(u => u.questionId === questionId);

  const rows = [
    ["ID", "Name", "Score", "AI Score", "Similarity"],
    ...filtered.map(u => [
      u.id,
      u.studentName,
      u.score ?? "N/A",
      u.llmScore ?? "N/A",
      u.similarity ?? "N/A"
    ])
  ];

  const csv =
    "data:text/csv;charset=utf-8," +
    rows.map(r => r.join(",")).join("\n");

  const link = document.createElement("a");
  link.href = encodeURI(csv);
  link.download = `model_${questionId}.csv`;
  link.click();
};

//PDF (MODEL-WISE)
const exportModelPDF = (questionId) => {
  const filtered = uploads.filter(u => u.questionId === questionId);

  const doc = new jsPDF();

  const tableData = filtered.map(u => [
    u.id,
    u.studentName,
    u.score ?? "N/A",
    u.llmScore ?? "N/A",
    u.similarity ?? "N/A"
  ]);

  autoTable(doc, {
    head: [["ID", "Name", "Score", "AI", "Similarity"]],
    body: tableData,
  });

  doc.save(`model_${questionId}.pdf`);
};
// ✅ EXPORT SINGLE CSV
// const exportSingleCSV = (u) => {
//   const rows = [
//     ["ID", "Name", "Score", "AI Score", "Similarity"],
//     [u.id, u.studentName, u.score ?? "N/A", u.llmScore ?? "N/A", u.similarity ?? "N/A"]
//   ];

//   const csv =
//     "data:text/csv;charset=utf-8," +
//     rows.map(r => r.join(",")).join("\n");

//   const link = document.createElement("a");
//   link.href = encodeURI(csv);
//   link.download = `result_${u.id}.csv`;
//   link.click();
// };

// // ✅ EXPORT SINGLE PDF
// const exportSinglePDF = (u) => {
//   const doc = new jsPDF();

//   autoTable(doc, {
//     head: [["ID", "Name", "Score", "AI", "Similarity"]],
//     body: [[u.id, u.studentName, u.score, u.llmScore, u.similarity]],
//   });

//   doc.save(`result_${u.id}.pdf`);
// };
  // 📊 CHART DATA
  const chartData = uploads.map(u => ({
    name: u.studentName,
    score: u.score || 0
  }));

  const pieData = [
    { name: "Pass", value: uploads.filter(u => (u.score || 0) >= 5).length },
    { name: "Fail", value: uploads.filter(u => (u.score || 0) < 5).length }
  ];

const COLORS = [
  "#22c55e",
  "#ef4444",
  "#3b82f6",
  "#f59e0b",
  "#8b5cf6",
  "#ec4899"
];
  return (
    <div className="layout">

      {/* SIDEBAR */}
      <div className="sidebar">
        <h2>SmartScan</h2>

        <button className={active==="overview"?"active":""} onClick={()=>setActive("overview")}>Overview</button>
        <button className={active==="model"?"active":""} onClick={()=>setActive("model")}>Create Model</button>
        <button className={active==="upload"?"active":""} onClick={()=>setActive("upload")}>Upload</button>
        <button className={active==="sheets"?"active":""} onClick={()=>setActive("sheets")}>Uploaded Sheets</button>
        <button className={active==="results"?"active":""} onClick={()=>setActive("results")}>Results</button>

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
            <>
              <div className="card">
                <h3>SmartScan AI Evaluation</h3>
                <p className="desc">
                  Evaluate answer sheets automatically using AI.
                </p>

                <div className="stat-box">
                  <p>Total Uploads</p>
                  <h2>{uploadCount}</h2>
                </div>
              </div>
              

{/* EXPORT BUTTONS */}
<div style={{ marginTop: "20px" }}>
  <button className="primary-btn" onClick={exportCSV}>
    Export CSV
  </button>

  <button
    className="primary-btn"
    onClick={exportPDF}
    style={{ marginLeft: "10px" }}
  >
    Export PDF
  </button>
</div>
              {/* ANALYTICS */}
              <div className="analytics-grid">
                <div className="analytics-card">
                  <h4>Average Score</h4>
                  <h2>
                    {uploads.length
                      ? (uploads.reduce((s,u)=>s+(u.score||0),0)/uploads.length).toFixed(1)
                      : "0"}
                  </h2>
                </div>

                <div className="analytics-card">
                  <h4>Avg Similarity</h4>
                  <h2>
                    {uploads.length
                      ? (uploads.reduce((s,u)=>s+(u.similarity||0),0)/uploads.length).toFixed(1)
                      : "0"}
                  </h2>
                </div>

                <div className="analytics-card">
                  <h4>Top Score</h4>
                  <h2>
                    {uploads.length
                      ? Math.max(...uploads.map(u=>u.score||0))
                      : "0"}
                  </h2>
                </div>
              </div>

              {/* CHARTS */}
             {/* CHARTS */}
<div className="charts-grid">

  {/* STUDENT SCORES */}
  <div className="chart-card">
    <h4>Student Scores</h4>
    <ResponsiveContainer width="100%" height={250}>
      <BarChart data={chartData} margin={{ top: 20, right: 250, left: 30, bottom: 50 }}>
        <XAxis
          dataKey="name"
          angle={-30}
          textAnchor="end"
          interval={0}
          height={60}
        />
        <YAxis/>
        <Tooltip/>
        <Bar dataKey="score" fill="#3b82f6"/>
      </BarChart>
    </ResponsiveContainer>
  </div>

  {/* PASS vs FAIL */}
  <div className="chart-card">
    <h4>Pass vs Fail</h4>
    <ResponsiveContainer width="100%" height={250}>
      <PieChart>
        <Pie data={pieData} dataKey="value" outerRadius={80}>
          {pieData.map((_,i)=>(<Cell key={i} fill={COLORS[i]}/>))}
        </Pie>
        <Legend/>
      </PieChart>
    </ResponsiveContainer>
  </div>

  {/* 🔥 ADD THIS - MODEL DISTRIBUTION */}
  <div className="chart-card">
    <h4>Model Distribution</h4>
    <ResponsiveContainer width="100%" height={250}>
      <PieChart>
        <Pie
          data={modelWiseData.map(m => ({
            name: m.model,
            value: m.total
          }))}
          dataKey="value"
          nameKey="name"
          outerRadius={80}
          label
        >
          {modelWiseData.map((_, i) => (
            <Cell key={i} fill={COLORS[i % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </ResponsiveContainer>
  </div>

  {/* 🔥 ADD THIS - MODEL AVG SCORE */}
  <div className="chart-card">
    <h4>Avg Score per Model</h4>
    <ResponsiveContainer width="100%" height={250}>
      <BarChart data={modelWiseData}>
        <XAxis dataKey="model" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="avgScore" fill="#6366f1" />
      </BarChart>
    </ResponsiveContainer>
  </div>

</div>
              {/* MODEL-WISE ANALYSIS */}
<div className="card">
  <h3>Model-wise Analysis</h3>
  <table className="upload-table">
    <thead>
      <tr>
        <th>Model</th>
        <th>Total</th>
        <th>Avg Score</th>
        <th>Actions</th>    
      </tr>
    </thead>
    <tbody>
      {modelWiseData.map((m, i) => (
        <tr key={i}>
          <td>{m.model}</td>
          <td>{m.total}</td>
          <td>{m.avgScore}</td>
          <td>
              <button className="primary-btn" onClick={() => exportModelCSV(m.questionId)}>CSV</button>
              <button className="primary-btn" onClick={() => exportModelPDF(m.questionId)}>PDF</button>
          </td>
        </tr>
      ))}
    </tbody>
  </table>
</div>
            </>
          )}

          {/* RESULTS */}
          {active === "results" && (
            <div className="card">
              <h2>Evaluation Results</h2>

              <table className="upload-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Student Name</th>
                    <th>Final Score</th>
                    <th>AI Score</th>
                    <th>Similarity</th>
                    <th>Feedback</th>
                  </tr>
                </thead>

                <tbody>
                  {uploads.map((u)=>(
                    <tr key={u.id}>
                      <td>{u.id}</td>
                      <td>{u.studentName}</td>

                      <td style={{color: u.score>=5?"green":"red"}}>
                        {u.score ?? "N/A"}
                      </td>

                      <td>{u.llmScore ?? "N/A"}</td>

                      <td>
                        {u.similarity?.toFixed(2) ?? "N/A"}
                      </td>

                      <td>
                        {u.feedback ? u.feedback.substring(0,60)+"..." : "N/A"}
                        <br/>
                        <button
                          onClick={()=>navigate(`/evaluation-result/${u.id}`)}
                          style={{
                            marginTop:"5px",
                            fontSize:"12px",
                            padding:"4px 10px",
                            borderRadius:"6px",
                            border:"1px solid #2563eb",
                            background:"#eff6ff",
                            color:"#2563eb"
                          }}
                        >
                          View
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
{/* CREATE MODEL */}
{active === "model" && (
  <div className="card">
    <h2>Create Model Answer</h2>

    <form className="form-container" onSubmit={handleCreateModel}>
      <input
        placeholder="Model Name"
        value={title}
        onChange={(e)=>setTitle(e.target.value)}
      />

      <textarea
        className="model-answer"
        placeholder="Model Answer"
        value={modelAnswer}
        onChange={(e)=>setModelAnswer(e.target.value)}
      />

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

      <select
        value={questionId}
        onChange={(e)=>setQuestionId(e.target.value)}
      >
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
          {/* SHEETS */}
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
                      <td>{u.id}</td>
                      <td>{u.studentName}</td>
                      <td><span className="status">{u.status}</span></td>

                      <td>
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