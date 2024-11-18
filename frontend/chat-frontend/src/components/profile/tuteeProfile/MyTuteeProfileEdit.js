import React, { useState } from "react";
import axios from "../../axios/AxiosInstance";
import "./MyTuteeProfileEdit.css";

const subjectDescriptions = {
  ELEMENTARY_MATH: "초등수학",
  MIDDLE_MATH: "중등수학",
  HIGH_MATH: "고등수학",
  ELEMENTARY_ENGLISH: "초등영어",
  MIDDLE_ENGLISH: "중등영어",
  HIGH_ENGLISH: "고등영어",
};

const tuteeGradeOptions = [
  { value: "ELEMENTARY_1", label: "초등학교 1학년" },
  { value: "ELEMENTARY_2", label: "초등학교 2학년" },
  { value: "ELEMENTARY_3", label: "초등학교 3학년" },
  { value: "ELEMENTARY_4", label: "초등학교 4학년" },
  { value: "ELEMENTARY_5", label: "초등학교 5학년" },
  { value: "ELEMENTARY_6", label: "초등학교 6학년" },
  { value: "MIDDLE_1", label: "중학교 1학년" },
  { value: "MIDDLE_2", label: "중학교 2학년" },
  { value: "MIDDLE_3", label: "중학교 3학년" },
  { value: "HIGH_1", label: "고등학교 1학년" },
  { value: "HIGH_2", label: "고등학교 2학년" },
  { value: "HIGH_3", label: "고등학교 3학년" },
];

const locationOptions = [
  { value: "SEOUL", label: "서울" },
  { value: "BUSAN", label: "부산" },
  { value: "INCHEON", label: "인천" }
];

const genderOptions = [
  { value: "MALE", label: "남성" },
  { value: "FEMALE", label: "여성" },
];

const MyTuteeProfileEdit = ({ memberInfo, tuteeProfile, onCancel, onUpdate }) => {
  const [form, setForm] = useState({
    nickname: memberInfo.nickname || "",
    gender: memberInfo.gender || "",
    tuteeName: tuteeProfile.tuteeName || "",
    subjects: tuteeProfile.subjects || [],
    location: tuteeProfile.location || "",
    description: tuteeProfile.description || "",
    tuteeGrade: tuteeProfile.tuteeGrade || "",
    gender: tuteeProfile.gender || "",
  });

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubjectToggle = (subject) => {
    setForm((prev) => ({
      ...prev,
      subjects: prev.subjects.includes(subject)
        ? prev.subjects.filter((s) => s !== subject)
        : [...prev.subjects, subject],
    }));
  };

  const handleSubmit = async () => {
    const updateRequest = {
      nickname: form.nickname,
      gender: form.gender,
      tuteeProfile: {
        tuteeId: tuteeProfile.tuteeId,
        tuteeName: form.tuteeName,
        gender: form.gender,
        tuteeGrade: form.tuteeGrade,
        subjects: form.subjects,
        location: form.location,
        description: form.description,
      }
    };

      try {
        await axios.put(`${process.env.REACT_APP_BACKEND_URL}/me`, updateRequest);
        alert("프로필이 수정되었습니다.");
        onUpdate(); // 수정 완료 후 부모 컴포넌트에서 데이터 새로고침
      } catch (error) {
        console.error(error);
        alert("수정에 실패했습니다.");
      }

    };


  return (
    <div className="profile-section">
      <h3>프로필 수정</h3>

      {/* 닉네임 */}
      <div className="input-group">
        <label>닉네임</label>
        <input
          type="text"
          value={form.nickname}
          onChange={(e) => handleChange("nickname", e.target.value)}
        />
      </div>

      <div className="input-group">
        <label>이름</label>
        <input
          value={form.tuteeName}
          onChange={(e) => handleChange("tuteeName", e.target.value)}
        />
      </div>  

      {/* 성별 선택 */}
      <div className="input-group">
        <label>성별</label>
        <select
          value={form.gender}
          onChange={(e) => handleChange("gender", e.target.value)}
        >
          <option value="">성별 선택</option>
          {genderOptions.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      {/* 학년 선택 */}
      <div className="input-group">
        <label>학년</label>
        <select
          value={form.tuteeGrade}
          onChange={(e) => handleChange("tuteeGrade", e.target.value)}
        >
          <option value="">학년 선택</option>
          {tuteeGradeOptions.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      {/* 과목 선택 */}
      <div className="input-group">
        <label>과목</label>
        <div className="chip-container">
          {Object.keys(subjectDescriptions).map((subject) => (
            <div
              key={subject}
              className={`chip ${form.subjects.includes(subject) ? "selected" : ""}`}
              onClick={() => handleSubjectToggle(subject)}
            >
              {subjectDescriptions[subject]}
            </div>
          ))}
        </div>
      </div>

      {/* 위치 선택 */}
      <div className="input-group">
        <label>위치</label>
        <select
          value={form.location}
          onChange={(e) => handleChange("location", e.target.value)}
        >
          <option value="">위치 선택</option>
          {locationOptions.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      {/* 소개 */}
      <div className="input-group">
        <label>소개</label>
        <textarea
          value={form.description}
          onChange={(e) => handleChange("description", e.target.value)}
        />
      </div>

      {/* 저장 및 취소 버튼 */}
      <div className="button-group">
        <button className="cancel-button" onClick={onCancel}>
          취소
        </button>
        <button className="save-button" onClick={handleSubmit}>
          저장
        </button>
      </div>
    </div>
  );
};

export default MyTuteeProfileEdit;
