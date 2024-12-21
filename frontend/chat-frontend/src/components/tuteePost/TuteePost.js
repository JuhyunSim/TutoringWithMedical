import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import InquiryButton from '../button/InquiryButton';
import axios from '../axios/AxiosInstance';
import BackButton from '../button/BackButton';
import './TuteePost.css';

const TuteePost = () => {
    const { postingId } = useParams();
    const navigate = useNavigate();
    const [postDetails, setPostDetails] = useState(null);

    useEffect(() => {
        const fetchPostDetails = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/tutee/posting/${postingId}`);
                setPostDetails(response.data);
            } catch (error) {
                console.error('Failed to fetch post details:', error);
            }
        };

        fetchPostDetails();
    }, [postingId]);

    const handleSendMessage = async (recipientId, message) => {
        console.log(`Sending message to ${recipientId}: ${message}`);
        try {
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/chat/start`, {
                recipientId: recipientId,
                message: message
            });
            const roomId = response.data;

            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/chat/${roomId}`, {
                recipientId: recipientId,
                message: message
            });

            console.log('Message sent, roomId:', roomId);
        } catch (error) {
            console.error('Failed to send message:', error);
        }
    };

    if (!postDetails) {
        return <div>Loading...</div>;
    }

    return (
        <div className="tutee-post-details-container">
            <h2>게시물 상세보기</h2>
            <div className="posting-details">
                <p><strong>학생 이름:</strong> {postDetails.memberNickname}</p>
                <p><strong>학년:</strong> {postDetails.studentGrade}</p>
                <p><strong>학교:</strong> {postDetails.studentSchool}</p>
                <p><strong>성격:</strong> {postDetails.personality}</p>
                <p><strong>수업 형태:</strong> {postDetails.tutoringType}</p>
                <p><strong>가능한 일정:</strong> {postDetails.possibleSchedule}</p>
                <p><strong>수업 수준:</strong> {postDetails.level}</p>
                <p><strong>수업료:</strong> {postDetails.fee}원</p>
                <p><strong>설명:</strong> {postDetails.description}</p>
                <p><strong>작성일:</strong> {new Date(postDetails.createdAt).toLocaleDateString()}</p>
                <p><strong>수정일:</strong> {new Date(postDetails.updatedAt).toLocaleDateString()}</p>
            </div>
            <div className="post-actions">
                 <BackButton />
                <InquiryButton
                    recipientId={postDetails.memberId}
                    memberNickname={postDetails.memberNickname}
                    onSendMessage={handleSendMessage}
                />
            </div>
        </div>
    );
};

export default TuteePost;
