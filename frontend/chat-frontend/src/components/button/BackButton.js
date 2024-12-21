import React from 'react';
import { useNavigate } from 'react-router-dom';
import './BackButton.css'; // 스타일 파일

const BackButton = ({ label = '돌아가기', className = '' }) => {
    const navigate = useNavigate();

    return (
        <button onClick={() => navigate(-1)} className={`back-button ${className}`}>
            {label}
        </button>
    );
};

export default BackButton;
