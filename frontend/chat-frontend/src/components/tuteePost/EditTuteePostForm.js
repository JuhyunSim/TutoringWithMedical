// EditTuteePostForm.js
import React, { useState, useEffect } from 'react';
import axios from '../axios/AxiosInstance';
import { useNavigate, useParams } from 'react-router-dom';
import './EditTuteePostForm.css';

const EditTuteePostForm = () => {
    const { postingId } = useParams(); // URL에서 postingId를 가져옴
    const [personality, setPersonality] = useState('');
    const [tutoringType, setTutoringType] = useState('');
    const [possibleSchedule, setPossibleSchedule] = useState('');
    const [description, setDescription] = useState('');
    const [fee, setFee] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        // 초기 데이터 불러오기
        axios.get(`/tutee/posting/${postingId}`)
            .then(response => {
                const { personality, tutoringType, possibleSchedule, description, fee } = response.data;
                setPersonality(personality);
                setTutoringType(tutoringType);
                setPossibleSchedule(possibleSchedule);
                setDescription(description);
                setFee(fee);
            })
            .catch(error => console.error('Failed to fetch post:', error));
    }, [postingId]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.patch(`/tutee/posting/${postingId}`, {
                personality,
                tutoringType,
                possibleSchedule,
                description,
                fee
            });
            alert('게시물이 성공적으로 수정되었습니다.');
            navigate('/my-profile/my-posts'); // 수정 후 게시물 목록으로 이동
        } catch (error) {
            console.error('Failed to update post:', error);
            alert('게시물 수정에 실패했습니다.');
        }
    };

    return (
        <div className="edit-post-form">
            <h2>게시물 수정</h2>
            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label htmlFor="personality">성격</label>
                    <input
                        type="text"
                        id="personality"
                        value={personality}
                        onChange={(e) => setPersonality(e.target.value)}
                        required
                    />
                </div>
                
                <div className="input-group">
                    <label htmlFor="tutoringType">수업 유형</label>
                    <select
                        id="tutoringType"
                        value={tutoringType}
                        onChange={(e) => setTutoringType(e.target.value)}
                        required
                    >
                        <option value="">선택하세요</option>
                        <option value="ON_LINE">온라인</option>
                        <option value="OFF_LINE">오프라인</option>
                    </select>
                </div>

                <div className="input-group">
                    <label htmlFor="possibleSchedule">가능한 시간</label>
                    <input
                        type="text"
                        id="possibleSchedule"
                        value={possibleSchedule}
                        onChange={(e) => setPossibleSchedule(e.target.value)}
                        required
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="description">설명</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        rows="4"
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="fee">수업료 (만원)</label>
                    <input
                        type="number"
                        id="fee"
                        value={fee}
                        onChange={(e) => setFee(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" className="submit-button">수정하기</button>
            </form>
        </div>
    );
};

export default EditTuteePostForm;
