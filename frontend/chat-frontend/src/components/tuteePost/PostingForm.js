import React, { useState } from 'react';
import axios from '../axios/AxiosInstance';
import { useNavigate } from 'react-router-dom';
import './PostingForm.css';

// 학년 옵션 리스트
const gradeOptions = [
    { value: "ELEMENTARY_1", label: "초1" },
    { value: "ELEMENTARY_2", label: "초2" },
    { value: "ELEMENTARY_3", label: "초3" },
    { value: "ELEMENTARY_4", label: "초4" },
    { value: "ELEMENTARY_5", label: "초5" },
    { value: "ELEMENTARY_6", label: "초6" },
    { value: "MIDDLE_1", label: "중1" },
    { value: "MIDDLE_2", label: "중2" },
    { value: "MIDDLE_3", label: "중3" },
    { value: "HIGH_1", label: "고1" },
    { value: "HIGH_2", label: "고2" },
    { value: "HIGH_3", label: "고3" }
];

const PostingForm = () => {
    const [tuteeGrade, setTuteeGrade] = useState('');
    const [school, setSchool] = useState('');
    const [personality, setPersonality] = useState('');
    const [tutoringType, setTutoringType] = useState('');
    const [possibleSchedule, setPossibleSchedule] = useState('');
    const [level, setLevel] = useState('');
    const [fee, setFee] = useState('');
    const [description, setDescription] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        const postData = {
            tuteeGrade,
            school,
            personality,
            tutoringType,
            possibleSchedule,
            level,
            fee: parseInt(fee),
            description,
        };

        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/tutee/posting`, postData);
            setSuccess('게시물이 성공적으로 등록되었습니다!');
            navigate('/posts');
            // 필요한 경우 필드를 초기화
        } catch (err) {
            setError('게시물 등록에 실패했습니다. 입력값을 확인하세요.');
            console.error(err);
        }
    };

    return (
        <div className="posting-form-container">
            <h2>과외 구인 글 작성하기</h2>
            <form onSubmit={handleSubmit} className="posting-form">
                <div className="form-group">
                    <label>학년</label>
                    <select 
                        value={tuteeGrade} 
                        onChange={(e) => setTuteeGrade(e.target.value)} 
                        required
                    >
                        <option value="">학년 선택</option>
                        {gradeOptions.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>학교</label>
                    <input type="text" value={school} onChange={(e) => setSchool(e.target.value)} required />
                </div>

                <div className="form-group">
                    <label>성격</label>
                    <input type="text" value={personality} onChange={(e) => setPersonality(e.target.value)} />
                </div>

                <div className="form-group">
                    <label>수업 유형</label>
                    <select value={tutoringType} onChange={(e) => setTutoringType(e.target.value)} required>
                        <option value="">수업 유형 선택</option>
                        <option value="ON_LINE">온라인</option>
                        <option value="OFF_LINE">오프라인</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>가능한 시간대</label>
                    <input type="text" value={possibleSchedule} onChange={(e) => setPossibleSchedule(e.target.value)} required />
                </div>

                <div className="form-group">
                    <label>수준</label>
                    <input type="text" value={level} onChange={(e) => setLevel(e.target.value)} />
                </div>

                <div className="form-group">
                <label>수업료</label>
                    <div className="fee-input-container">
                        <input 
                            type="number" 
                            value={fee} 
                            min="0" // 음수 입력 방지
                            onChange={(e) => setFee(e.target.value)} 
                            required 
                        />
                        <span className="fee-unit">만원</span>
                    </div>
                </div>

                <div className="form-group">
                    <label>설명</label>
                    <textarea value={description} onChange={(e) => setDescription(e.target.value)}></textarea>
                </div>

                <button type="submit" className="submit-button">게시하기</button>
                {error && <p className="error">{error}</p>}
                {success && <p className="success">{success}</p>}
            </form>
        </div>
    );
};

export default PostingForm;
