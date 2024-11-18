import React, { useState } from "react";
import MyTuteeProfileEdit from "./MyTuteeProfileEdit";
import "./MyTuteeProfile.css";

const subjectDescriptions = {
  ELEMENTARY_MATH: "초등수학",
  MIDDLE_MATH: "중등수학",
  HIGH_MATH: "고등수학",
  ELEMENTARY_ENGLISH: "초등영어",
  MIDDLE_ENGLISH: "중등영어",
  HIGH_ENGLISH: "고등영어",
};

const MyTuteeProfile = ({ memberInfo, tuteeProfiles, refreshProfile }) => {
  const [isEditing, setIsEditing] = useState(false);

  if (!tuteeProfiles || tuteeProfiles.length === 0) {
    return <div className="profile-section">학생 프로필이 없습니다.</div>;
  }

  const tuteeProfile = tuteeProfiles[0]; // 항상 첫 번째 프로필 사용

  if (isEditing) {
    return (
      <MyTuteeProfileEdit
        memberInfo={memberInfo}
        tuteeProfile={tuteeProfile}
        onCancel={() => setIsEditing(false)} // 수정 취소 시
        onUpdate={() => {
          refreshProfile(); // 수정 후 데이터 새로고침
          setIsEditing(false); // 수정 모드 종료
        }}
      />
    );
  }

  return (
    <div className="profile-section">
      <p>
        <strong>닉네임:</strong> {memberInfo.nickname}
      </p>
      <p>
        <strong>이름:</strong> {tuteeProfile.tuteeName || "정보 없음"}
      </p>
      <p>
        <strong>성별:</strong> {memberInfo.gender === "MALE" ? "남성" : "여성"}
      </p>
      <p>
        <strong>학년:</strong> {tuteeProfile.tuteeGrade || "정보 없음"}
      </p>
      <p>
        <strong>과목:</strong>{" "}
        {tuteeProfile.subjects && tuteeProfile.subjects.length > 0
          ? tuteeProfile.subjects
              .map((subject) => subjectDescriptions[subject] || subject)
              .join(", ")
          : "선택된 과목이 없습니다."}
      </p>
      <p>
        <strong>위치:</strong> {tuteeProfile.location || "정보 없음"}
      </p>
      <p>
        <strong>소개:</strong> {tuteeProfile.description || "소개가 없습니다."}
      </p>
      <button className="edit-button" onClick={() => setIsEditing(true)}>
        수정
      </button>
    </div>
  );
};

export default MyTuteeProfile;
