import React, { useState } from "react";
import axios from "../../axios/AxiosInstance";
import "./MyTutorProfileEdit.css";

const subjectDescriptions = {
  ELEMENTARY_MATH: "초등수학",
  MIDDLE_MATH: "중등수학",
  HIGH_MATH: "고등수학",
  ELEMENTARY_ENGLISH: "초등영어",
  MIDDLE_ENGLISH: "중등영어",
  HIGH_ENGLISH: "고등영어",
};

const genderOptions = [
  { value: "MALE", label: "남성" },
  { value: "FEMALE", label: "여성" },
];

const universityList = [
    { value: "SEOUL_UNIVERSITY", label: "서울대학교" },
    { value: "KOREA_UNIVERSITY", label: "고려대학교" },
    { value: "YONSEI_UNIVERSITY", label: "연세대학교" },
    { value: "SUNKYUNKWAN_UNIVERSITY", label: "성균관대학교" }
];

const locationOptions = [
    { value: "SEOUL", label: "서울" },
    { value: "BUSAN", label: "부산" },
    { value: "INCHEON", label: "인천" }
  ];

const MyTutorProfileEdit = ({ memberInfo, tutorProfile, onCancel, onUpdate }) => {
  const [form, setForm] = useState({
    nickname: memberInfo.nickname,
    gender: memberInfo.gender,
    proofFileUrl: tutorProfile.proofFileUrl,
    imageUrl: tutorProfile.imageUrl,
    subjects: tutorProfile.subjects || [],
    location: tutorProfile.location,
    description: tutorProfile.description,
    university: tutorProfile.university,
    status: tutorProfile.status,
  });

  const [previewImage, setPreviewImage] = useState(
    tutorProfile.imageUrl
      ? `${process.env.REACT_APP_BACKEND_URL}${tutorProfile.imageUrl}`
      : null
  );
  const [proofFileName, setProofFileName] = useState("");

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

  const handleFileUpload = async (e, field) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_BACKEND_URL}/api/images/upload`,
        formData,
        { headers: { "Content-Type": "multipart/form-data" } }
      );

      setForm((prev) => ({ ...prev, [field]: response.data }));

      if (field === "imageUrl") {
        const reader = new FileReader();
        reader.onload = () => setPreviewImage(reader.result);
        reader.readAsDataURL(file);
      } else if (field === "proofFileUrl") {
        setProofFileName(file.name);
      }
    } catch (error) {
      console.error("파일 업로드 실패:", error);
      alert("파일 업로드에 실패했습니다.");
    }
  };

  const handleSubmit = async () => {
    const updateRequest = {
      nickname: form.nickname,
      gender: form.gender,
      tutorProfile: {
        proofFileUrl: form.proofFileUrl,
        imageUrl: form.imageUrl,
        subjects: form.subjects,
        location: form.location,
        description: form.description,
        university: form.university,
        status: form.status,
      },
    };

    try {
      await axios.put(`${process.env.REACT_APP_BACKEND_URL}/me`, updateRequest);
      alert("프로필이 수정되었습니다.");
      onUpdate();
    } catch (error) {
      console.error(error);
      alert("수정에 실패했습니다.");
    }
  };

  return (
    <div className="profile-section">
      <h3>선생님 프로필 수정</h3>

      {/* 닉네임 */}
      <div className="input-group">
        <label>닉네임</label>
        <input
          type="text"
          value={form.nickname}
          onChange={(e) => handleChange("nickname", e.target.value)}
        />
      </div>

      {/* 성별 */}
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

      {/* 대학교 */}
      <div className="input-group">
        <label>대학교</label>
        <select
          value={form.university}
          onChange={(e) => handleChange("location", e.target.value)}
        >
            <option value="">학교 선택</option>
            {universityList.map((option) => (
                <option key={option.value} value={option.value}>
                    {option.label}
                </option>
            ))}
        </select>
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

      {/* 과목 */}
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

      {/* 이미지 업로드 */}
      <div className="input-group">
        <label>프로필 사진</label>
        <div className="profile-image-container">
          {previewImage ? (
            <img src={previewImage} alt="프로필 사진 미리보기" className="preview-image" />
          ) : (
            <div className="placeholder-image">이미지 없음</div>
          )}
        </div>
        <input
          type="file"
          accept="image/*"
          onChange={(e) => handleFileUpload(e, "imageUrl")}
        />
      </div>

      {/* 인증 파일 업로드 */}
      <div className="input-group">
        <label>인증 파일</label>
        <input
          type="file"
          accept=".pdf,.doc,.docx,.png,.jpg"
          onChange={(e) => handleFileUpload(e, "proofFileUrl")}
        />
        {proofFileName && <p>업로드된 파일: {proofFileName}</p>}
      </div>

      {/* 소개 */}
      <div className="input-group">
        <label>소개</label>
        <textarea
          value={form.description}
          onChange={(e) => handleChange("description", e.target.value)}
        />
      </div>

      {/* 저장 버튼 */}
      <button className="save-button" onClick={handleSubmit}>
        저장
      </button>
    </div>
  );
};

export default MyTutorProfileEdit;
