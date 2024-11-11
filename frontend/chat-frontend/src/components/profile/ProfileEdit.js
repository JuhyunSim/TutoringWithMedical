import React, { useState, useEffect } from 'react';
import axios from '../axios/AxiosInstance';
import './ProfileEdit.css';

const subjectsList = [
    { value: "ELEMENTARY_MATH", label: "초등수학" },
    { value: "MIDDLE_MATH", label: "중등수학" },
    { value: "HIGH_MATH", label: "고등수학" },
    { value: "ELEMENTARY_ENGLISH", label: "초등영어" },
    { value: "MIDDLE_ENGLISH", label: "중등영어" },
    { value: "HIGH_ENGLISH", label: "고등영어" },
];

const locationList = [
    { value: "SEOUL", label: "서울" },
    { value: "BUSAN", label: "부산" },
    { value: "INCHEON", label: "인천" },
];

const universityList = [
    { value: "SEOUL_UNIVERSITY", label: "서울대학교" },
    { value: "KOREA_UNIVERSITY", label: "고려대학교" },
    { value: "YONSEI_UNIVERSITY", label: "연세대학교" },
];

const enrollmentStatusList = [
    { value: "ENROLLED", label: "재학" },
    { value: "GRADUATED", label: "졸업" },
];

const ProfileEdit = () => {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        // 프로필 정보 가져오기
        const fetchProfile = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/me`);
                setProfile(response.data);
            } catch (err) {
                setError('프로필 정보를 불러오는 데 실패했습니다.');
            }
        };
        fetchProfile();
    }, []);

    const toggleSubject = (subject, profileType) => {
        const updateSubjects = (selectedSubjects) =>
            selectedSubjects.includes(subject)
                ? selectedSubjects.filter((s) => s !== subject)
                : [...selectedSubjects, subject];

        if (profileType === 'TUTOR') {
            setProfile((prev) => ({
                ...prev,
                tutorProfile: {
                    ...prev.tutorProfile,
                    subjects: updateSubjects(prev.tutorProfile.subjects || []),
                },
            }));
        } else if (profileType === 'TUTEE') {
            setProfile((prev) => ({
                ...prev,
                tuteeProfile: {
                    ...prev.tuteeProfile,
                    subjects: updateSubjects(prev.tuteeProfile.subjects || []),
                },
            }));
        }
    };

    const handleSubmit = async () => {
        setIsSubmitting(true);
        try {
            await axios.put(`${process.env.REACT_APP_BACKEND_URL}/me`, profile);
            alert('프로필이 성공적으로 수정되었습니다!');
        } catch (err) {
            alert('프로필 수정에 실패했습니다.');
            console.error(err);
        } finally {
            setIsSubmitting(false);
        }
    };

    if (error) return <div>{error}</div>;
    if (!profile) return <div>로딩 중...</div>;

    return (
        <div className="profile-edit">
            <h2>프로필 수정</h2>

            <div className="input-group">
                <label>닉네임</label>
                <input
                    type="text"
                    value={profile.nickname}
                    onChange={(e) => setProfile({ ...profile, nickname: e.target.value })}
                />
            </div>

            {/* 선생님 프로필 수정 */}
            {profile.tutorProfile && (
                <>
                    <h3>선생님 프로필</h3>
                    <div className="input-group">
                        <label>소속 대학교</label>
                        <select
                            value={profile.tutorProfile.university}
                            onChange={(e) =>
                                setProfile({
                                    ...profile,
                                    tutorProfile: { ...profile.tutorProfile, university: e.target.value },
                                })
                            }
                        >
                            <option value="">대학교 선택</option>
                            {universityList.map((uni) => (
                                <option key={uni.value} value={uni.value}>
                                    {uni.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="input-group">
                        <label>위치</label>
                        <select
                            value={profile.tutorProfile.location}
                            onChange={(e) =>
                                setProfile({
                                    ...profile,
                                    tutorProfile: { ...profile.tutorProfile, location: e.target.value },
                                })
                            }
                        >
                            <option value="">위치 선택</option>
                            {locationList.map((location) => (
                                <option key={location.value} value={location.value}>
                                    {location.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="input-group">
                        <label>등록 상태</label>
                        <select
                            value={profile.tutorProfile.status}
                            onChange={(e) =>
                                setProfile({
                                    ...profile,
                                    tutorProfile: { ...profile.tutorProfile, status: e.target.value },
                                })
                            }
                        >
                            <option value="">등록 상태 선택</option>
                            {enrollmentStatusList.map((status) => (
                                <option key={status.value} value={status.value}>
                                    {status.label}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="input-group">
                        <label>과목</label>
                        <div className="chip-container">
                            {subjectsList.map((subject) => (
                                <div
                                    key={subject.value}
                                    className={`chip ${
                                        profile.tutorProfile.subjects?.includes(subject.value) ? 'selected' : ''
                                    }`}
                                    onClick={() => toggleSubject(subject.value, 'TUTOR')}
                                >
                                    {subject.label}
                                </div>
                            ))}
                        </div>
                    </div>
                </>
            )}

            <button onClick={handleSubmit} disabled={isSubmitting}>
                저장
            </button>
        </div>
    );
};

export default ProfileEdit;
