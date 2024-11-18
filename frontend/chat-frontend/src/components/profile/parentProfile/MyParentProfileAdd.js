import React, { useState } from "react";
import axios from "../../axios/AxiosInstance";
import "./MyParentProfileAdd.css";

const subjectDescriptions = {
    ELEMENTARY_MATH: "초등수학",
    MIDDLE_MATH: "중등수학",
    HIGH_MATH: "고등수학",
    ELEMENTARY_ENGLISH: "초등영어",
    MIDDLE_ENGLISH: "중등영어",
    HIGH_ENGLISH: "고등영어",
  };
  
  const locationOptions = [
      { value: "SEOUL", label: "서울" },
      { value: "BUSAN", label: "부산" },
      { value: "INCHEON", label: "인천" },
      // Add more options as needed
    ];
    
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

const MyParentProfileAdd = ({ onBack, updateProfiles }) => {
  const [newProfile, setNewProfile] = useState({
    tuteeName: "",
    gender: "",
    location: "",
    subjects: [],
    description: "",
    tuteeGrade: "",
  });

  const handleInputChange = (field, value) => {
    setNewProfile((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const handleSubjectToggle = (subject) => {
    setNewProfile((prev) => {
      const updatedSubjects = prev.subjects.includes(subject)
        ? prev.subjects.filter((s) => s !== subject)
        : [...prev.subjects, subject];
      console.log("Updated subjects:", updatedSubjects); // 디버깅용 로그
      return { ...prev, subjects: updatedSubjects };
    });
  };
  

  const handleAddProfileSubmit = async () => {
    try {
      const response = await axios.put(`${process.env.REACT_APP_BACKEND_URL}/me/add-profile`, newProfile);
      alert("프로필이 추가되었습니다!");
            // 부모 컴포넌트의 상태 업데이트
      if (updateProfiles) {
          updateProfiles(response.data); // response.data에 새로 추가된 프로필 정보가 있다고 가정
      }
    } catch (err) {
      alert("프로필 추가에 실패했습니다. 입력 정보를 확인해주세요.");
      console.error(err);
    }
  };

  return (
    <div className="add-profile-container">
      <h3>자녀 프로필 추가</h3>
      <button className="back-button" onClick={onBack}>
        뒤로가기
      </button>

      <div className="input-group">
        <label>이름</label>
        <input
          type="text"
          onChange={(e) => handleInputChange("tuteeName", e.target.value)}
          placeholder="이름을 입력하세요"
        />
      </div>

      <div className="input-group">
        <label>성별</label>
        <select
          onChange={(e) => handleInputChange("gender", e.target.value)}
        >
          <option value="">성별 선택</option>
          <option value="MALE">남성</option>
          <option value="FEMALE">여성</option>
        </select>
      </div>
      <div className="input-group">
      <label>지역</label>
        <select
          value={newProfile.location}
          onChange={(e) => handleInputChange("location", e.target.value)}
        >
          <option value="">지역을 선택하세요</option>
          {locationOptions.map((location) => (
            <option key={location.value} value={location.value}>
              {location.label}
            </option>
          ))}
        </select>
      </div>
      <div className="input-group">
        <label>과목</label>
        <div className="chip-container">
          {Object.keys(subjectDescriptions).map((subject) => (
            <div
              key={subject}
              className={`chip ${newProfile.subjects.includes(subject) ? "selected" : ""}`}
              onClick={() => handleSubjectToggle(subject)}
            >
              {subjectDescriptions[subject]}
            </div>
          ))}
        </div>
      </div>
      <div className="input-group">
      <div className="input-group">
      <label>학년</label>
        <select
          value={newProfile.tuteeGrade}
          onChange={(e) => handleInputChange("tuteeGrade", e.target.value)}
        >
          <option value="">학년을 선택하세요</option>
          {tuteeGradeOptions.map((grade) => (
            <option key={grade.value} value={grade.value}>
              {grade.label}
            </option>
          ))}
        </select>
      </div>
      </div>
      <div className="input-group">
        <label>소개</label>
        <textarea
          value={newProfile.description}
          onChange={(e) => handleInputChange("description", e.target.value)}
          placeholder="소개를 입력하세요"
        ></textarea>
      </div>
      <button className="submit-button" onClick={handleAddProfileSubmit}>
        추가
      </button>
    </div>
  );
};

export default MyParentProfileAdd;
