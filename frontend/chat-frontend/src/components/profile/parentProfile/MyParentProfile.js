import React, { useState } from "react";
import axios from "../../axios/AxiosInstance";
import MyParentProfileAdd from "./MyParentProfileAdd";
import MyParentProfileEdit from "./MyParentProfileEdit";
import "./MyParentProfile.css";

const subjectDescriptions = {
  ELEMENTARY_MATH: "초등수학",
  MIDDLE_MATH: "중등수학",
  HIGH_MATH: "고등수학",
  ELEMENTARY_ENGLISH: "초등영어",
  MIDDLE_ENGLISH: "중등영어",
  HIGH_ENGLISH: "고등영어",
};
const MyParentProfile = ({
  parentInfo,
  tuteeProfiles,
  selectedProfileIndex,
  setSelectedProfileIndex,
  refreshProfiles,
}) => {
  const [isEditing, setIsEditing] = useState(false);
  const [isAdding, setIsAdding] = useState(false);

  const handleUpdate = async (tuteeId, updatedProfile) => {
    try {
      // 데이터 새로고침
      await refreshProfiles();

      // tuteeProfiles가 정렬된 상태에서 수정된 데이터의 새로운 위치 계산
      const sortedProfiles = [...tuteeProfiles].sort((a, b) => a.tuteeId - b.tuteeId);
      const newIndex = sortedProfiles.findIndex((profile) => profile.tuteeId === tuteeId);

      // 인덱스 동기화
      setSelectedProfileIndex(newIndex);
      setIsEditing(false);
    } catch (error) {
      console.error("프로필 수정 실패:", error);
    }
  };

  const handleProfileSelect = (index) => {
    setSelectedProfileIndex(index); // 선택된 인덱스 업데이트
  };

  const handleDeleteProfile = async (tuteeId) => {
    if (window.confirm("정말 이 프로필을 삭제하시겠습니까?")) {
      try {
        await axios.delete(`${process.env.REACT_APP_BACKEND_URL}/parent/tutee/${tuteeId}`);
        await refreshProfiles(); // 데이터 갱신
        setSelectedProfileIndex(0); // 첫 번째 프로필로 초기화
        alert("프로필이 삭제되었습니다.");
      } catch (error) {
        console.error("프로필 삭제 실패:", error);
        alert("프로필 삭제에 실패했습니다.");
      }
    }
  };

  if (isAdding) {
    return (
      <MyParentProfileAdd
        onBack={() => setIsAdding(false)}
        updateProfiles={refreshProfiles} // 새 프로필 추가 후 데이터 갱신
      />
    );
  }

  if (isEditing) {
    return (
      <MyParentProfileEdit
        parentInfo={parentInfo}
        tuteeProfile={tuteeProfiles[selectedProfileIndex]} // 선택된 프로필 사용
        onCancel={() => setIsEditing(false)}
        onUpdate={handleUpdate}
      />
    );
  }

  return (
    <div className="profile-section">
      <div className="profile-header">
        <h2>학생 프로필</h2>
        <button
          className="edit-button"
          onClick={() => setIsEditing(true)} // 수정 버튼 클릭 시 MyParentProfileEdit 호출
        >
          수정
        </button>
      </div>

      <div className="profile-info">
        <h3>회원 정보</h3>
        <p>
          <strong>닉네임:</strong> {parentInfo.nickname}
        </p>
        <p>
          <strong>성별:</strong> {parentInfo.gender === "MALE" ? "남성" : "여성"}
        </p>
        <p>
          <strong>역할:</strong> 학부모
        </p>
      </div>

      {/* 학생 선택 버튼 */}
      <div className="student-buttons">
        {tuteeProfiles
          .sort((a, b) => a.tuteeId - b.tuteeId) // 항상 tuteeId 기준 정렬
          .map((_, index) => (
            <button
              key={index}
              onClick={() => handleProfileSelect(index)}
              className={selectedProfileIndex === index ? "active" : ""}
            >
              학생 {index + 1} 프로필
            </button>
          ))}
      </div>

      {/* 선택된 학생 프로필 */}
      <div className="child-profile">
        <div className="profile-header">
          <h4>학생 {selectedProfileIndex + 1} 프로필</h4>
          <div className="button-group">
            <button
              className="delete-button"
              onClick={() => handleDeleteProfile(tuteeProfiles[selectedProfileIndex].tuteeId)}
            >
              삭제
            </button>
          </div>
        </div>
        <p>
          <strong>이름:</strong> {tuteeProfiles[selectedProfileIndex]?.tuteeName}
        </p>
        <p>
          <strong>성별:</strong> {tuteeProfiles[selectedProfileIndex]?.gender || "정보 없음"}
        </p>
        <p>
          <strong>학년:</strong> {tuteeProfiles[selectedProfileIndex]?.tuteeGrade}
        </p>
        <p>
          <strong>과목:</strong>{" "}
          {tuteeProfiles[selectedProfileIndex]?.subjects
            .map((subject) => subjectDescriptions[subject])
            .join(", ")}
        </p>
        <p>
          <strong>위치:</strong> {tuteeProfiles[selectedProfileIndex]?.location}
        </p>
        <p>
          <strong>소개:</strong>{" "}
          {tuteeProfiles[selectedProfileIndex]?.description || "소개가 없습니다."}
        </p>
      </div>

      {/* 추가 버튼 */}
      <button className="add-button" onClick={() => setIsAdding(true)}>
        학생 프로필 추가
      </button>
    </div>
  );
};

export default MyParentProfile;
