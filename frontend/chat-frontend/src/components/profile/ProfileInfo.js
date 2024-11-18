import React, { useEffect, useState } from "react";
import axios from "../axios/AxiosInstance";
import MyTutorProfile from "./tutorProfile/MyTutorProfile";
import MyTuteeProfile from "./tuteeProfile/MyTuteeProfile";
import MyParentProfile from "./parentProfile/MyParentProfile";
import "./ProfileInfo.css";

const ProfileInfo = () => {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);
  const [selectedProfileIndex, setSelectedProfileIndex] = useState(0); // 선택된 학생 프로필 인덱스

  // 프로필 데이터 불러오기
  const fetchProfile = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/me`);
      console.log("API 응답:", response.data); // 디버깅용
      setProfile(response.data);
    } catch (err) {
      console.error("프로필 로드 실패:", err);
      setError("프로필 정보를 불러오는 데 실패했습니다.");
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  if (error) return <div>{error}</div>;
  if (!profile) return <div>로딩 중...</div>;

  return (
    <div className="profile-info">
      <div className="profile-header">
        <h2>내 프로필</h2>
      </div>

      {/* 나의 프로필 */}
      {profile.role.includes("TUTOR") && (
        <MyTutorProfile
          memberInfo={profile}
          tutorProfile={profile.tutorProfile}
          refreshProfile={fetchProfile}
        />
      )}
      {profile.role.includes("TUTEE") && (
        <MyTuteeProfile 
        memberInfo={profile}
        tuteeProfiles={profile.tuteeProfiles || []}
        refreshProfile={fetchProfile} />
      )}
      {profile.role.includes("PARENT") && (
        <MyParentProfile
          parentInfo={profile}
          tuteeProfiles={profile.tuteeProfiles || []}
          selectedProfileIndex={selectedProfileIndex} // 상태 전달
          setSelectedProfileIndex={setSelectedProfileIndex} // 상태 업데이트 함수 전달
          refreshProfiles={fetchProfile} // 데이터 새로고침 전달
        />
      )}
    </div>
  );
};

export default ProfileInfo;
