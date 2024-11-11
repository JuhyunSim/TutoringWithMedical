import React, { useEffect, useState } from "react";
import axios from "../axios/AxiosInstance";
import { Link } from "react-router-dom";
import "./ProfileInfo.css";

const subjectDescriptions = {
  ELEMENTARY_MATH: "초등수학",
  MIDDLE_MATH: "중등수학",
  HIGH_MATH: "고등수학",
  ELEMENTARY_ENGLISH: "초등영어",
  MIDDLE_ENGLISH: "중등영어",
  HIGH_ENGLISH: "고등영어",
};

const ProfileInfo = () => {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/me`);
        setProfile(response.data);
      } catch (err) {
        setError("프로필 정보를 불러오는 데 실패했습니다.");
      }
    };
    fetchProfile();
  }, []);

  if (error) return <div>{error}</div>;
  if (!profile) return <div>로딩 중...</div>;

  return (
    <div className="profile-info">
      <div className="profile-header">
        <h2>나의 프로필</h2>
        <Link to="/me/profile/edit" className="edit-link">
          수정
        </Link>
      </div>
      <div className="profile-section">
        <p><strong>닉네임:</strong> {profile.nickname}</p>
        <p><strong>성별:</strong> {profile.gender === "MALE" ? "남성" : "여성"}</p>
      </div>
      {profile.role.includes("TUTOR") && (
        <div className="profile-section">
          <h3>선생님 프로필</h3>
          <p><strong>대학교:</strong> {profile.tutorProfile.university}</p>
          <p><strong>위치:</strong> {profile.tutorProfile.location}</p>
          <p>
            <strong>과목:</strong>{" "}
            {profile.tutorProfile.subjects
              .map((subject) => subjectDescriptions[subject])
              .join(", ")}
          </p>
          <p><strong>소개:</strong> {profile.tutorProfile.description || "소개가 없습니다."}</p>
        </div>
      )}
    </div>
  );
};

export default ProfileInfo;
