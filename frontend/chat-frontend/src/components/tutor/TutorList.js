// TutorList.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './TutorList.css';

const TutorList = () => {
    const [tutors, setTutors] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const pageSize = 10;

    useEffect(() => {
        fetchTutors();
    }, [currentPage]);

    const fetchTutors = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/tutor`);
            setTutors(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Failed to fetch tutor profiles:', error);
        }
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setCurrentPage(newPage);
        }
    };

    return (
        <div className="tutor-list-container">
            <h2>선생님 목록</h2>
            <div className="tutor-grid">
                {tutors.map(tutor => (
                    <div key={tutor.nickname} className="tutor-item">
                        <h3>{tutor.nickname}</h3>
                        <p>학교: {tutor.university}</p>
                        <p>지역: {tutor.location}</p>
                        <p>과목: {tutor.subjects.join(', ')}</p>
                        <Link to={`/tutor-profiles/${tutor.id}`} className="view-profile-button">프로필 보기</Link>
                    </div>
                ))}
            </div>

            {/* 페이지네이션 */}
            <div className="pagination">
                <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0}>&lt;</button>
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index}
                        onClick={() => handlePageChange(index)}
                        className={index === currentPage ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
                <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>&gt;</button>
            </div>
        </div>
    );
};

export default TutorList;
