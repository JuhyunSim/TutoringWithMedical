// TutorProfile.js
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from '../axios/AxiosInstance';
import Review from '../review/Review';
import './TutorProfile.css';

const TutorProfile = () => {
    const { tutorId } = useParams();
    const [profile, setProfile] = useState(null);
    const [showReviews, setShowReviews] = useState(false);

    useEffect(() => {
        fetchTutorProfile();
    }, [tutorId]);

    const fetchTutorProfile = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/tutor/${tutorId}`);
            setProfile(response.data);
        } catch (error) {
            console.error('Failed to fetch tutor profile:', error);
        }
    };

    const toggleShowReviews = () => {
        setShowReviews((prev) => !prev);
    };

    if (!profile) {
        return <p>Loading...</p>;
    }

    return (
        <div className="tutor-profile-container">
            <h2>{profile.nickname} 선생님 프로필</h2>
            <div className="profile-header">
                {profile.imageUrl ? (
                    <img
                        src={`${process.env.REACT_APP_BACKEND_URL}${profile.imageUrl}`}
                        alt={`${profile.nickname}의 프로필 사진`}
                        className="profile-image"
                    />
                ) : (
                    <div className="profile-placeholder">이미지 없음</div>
                )}
            </div>
            <p>성별: {profile.gender}</p>
            <p>과목: {profile.subjects.join(', ')}</p>
            <p>학교: {profile.university}</p>
            <p>지역: {profile.location}</p>
            <p>등록 상태: {profile.status}</p>
            <p>소개: {profile.description}</p>

            {/* 리뷰 버튼 */}
            <button
                className="review-toggle-button"
                onClick={toggleShowReviews}
            >
                {showReviews ? '리뷰 닫기' : '리뷰 보기'}
            </button>

            {/* 리뷰 표시 */}
            {showReviews && <Review tutorId={tutorId} />}
        </div>
    );
};

export default TutorProfile;
