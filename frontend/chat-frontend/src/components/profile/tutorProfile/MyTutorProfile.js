import React, { useState } from "react";
import MyTutorProfileEdit from "./MyTutorProfileEdit";
import "./MyTutorProfile.css";

const subjectDescriptions = {
  ELEMENTARY_MATH: "초등수학",
  MIDDLE_MATH: "중등수학",
  HIGH_MATH: "고등수학",
  ELEMENTARY_ENGLISH: "초등영어",
  MIDDLE_ENGLISH: "중등영어",
  HIGH_ENGLISH: "고등영어",
};

const MyTutorProfile = ({ memberInfo, tutorProfile, refreshProfile }) => {
  const [isEditing, setIsEditing] = useState(false); // 수정 모드 상태

  if (!tutorProfile) return null;

  // 수정 모드 렌더링
  if (isEditing) {
    return (
      <MyTutorProfileEdit
        memberInfo={memberInfo} // 닉네임과 성별 정보 전달
        tutorProfile={tutorProfile} // 튜터 프로필 정보 전달
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
      <h3>선생님 프로필</h3>
      <div className="profile-image-container">
        {tutorProfile.imageUrl ? (
          <img
            src={`${process.env.REACT_APP_BACKEND_URL}${tutorProfile.imageUrl}`}
            alt="프로필 사진"
            className="profile-image"
          />
        ) : (
          <div className="placeholder-image">이미지 없음</div>
        )}
      </div>
      <p>
        <strong>닉네임:</strong> {memberInfo.nickname}
      </p>
      <p>
        <strong>성별:</strong> {memberInfo.gender === "MALE" ? "남성" : "여성"}
      </p>
      <p>
        <strong>대학교:</strong> {tutorProfile.university}
      </p>
      <p>
        <strong>위치:</strong> {tutorProfile.location}
      </p>
      <p>
        <strong>과목:</strong>{" "}
        {tutorProfile.subjects
          .map((subject) => subjectDescriptions[subject])
          .join(", ")}
      </p>
      <p>
        <strong>소개:</strong> {tutorProfile.description || "소개가 없습니다."}
      </p>
      {/* 수정 버튼 추가 */}
      <button
        className="edit-button"
        onClick={() => setIsEditing(true)} // 수정 버튼 클릭 시
      >
        수정
      </button>
    </div>
  );
};

export default MyTutorProfile;
