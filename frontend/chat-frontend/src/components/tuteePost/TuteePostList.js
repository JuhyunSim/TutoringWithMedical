import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import qs from 'qs'; 
import axiosInstance from '../axios/AxiosInstance';
import InquiryButton from '../button/InquiryButton';
import SortButtonGroup from '../button/SortButtonGroup';
import './TuteePostList.css';
import { Link } from 'react-router-dom';

const TuteePostList = ({ memberId, memberRole }) => {
    const [postings, setPostings] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;
    const maxPageDisplay = 10;
    const [totalPages, setTotalPages] = useState(0); // API로 받은 총 페이지 수 저장
    const [sortCriteria, setSortCriteria] = useState([{ key: 'CREATED_AT', direction: 'DESC' }]);
    const [filters, setFilters] = useState({
        gender: '',
        tuteeGradeType: '',
        tutoringType: ''
    });
    const navigate = useNavigate();

    // 게시물 데이터 가져오기
    useEffect(() => {
        const fetchPostings = async () => {
            try {
                const response = await axios.get(
                    `${process.env.REACT_APP_BACKEND_URL}/tutee/postings`,
                    {
                        params: {
                            sortBy: sortCriteria.map((criterion) => criterion.key),
                            sortDirection: sortCriteria.map((criterion) => criterion.direction),
                            gender: filters.gender || null,
                            tuteeGradeType: filters.tuteeGradeType || null,
                            tutoringType: filters.tutoringType || null,
                            page: currentPage,
                            size: pageSize,
                        },
                        paramsSerializer: (params) => {
                            return qs.stringify(params, { arrayFormat: 'repeat' });
                        },
                    }
                );
                setPostings(response.data.content);
                setTotalPages(response.data.totalPages);
            } catch (error) {
                console.error('Failed to fetch postings:', error);
            }
        };

        fetchPostings();
    }, [currentPage, sortCriteria, filters]);

    // 필터 값 변경
    const handleFilterChange = (filterName, value) => {
        setFilters((prevFilters) => ({ ...prevFilters, [filterName]: value }));
    };

    const handleAddCriteria = () => {
        setSortCriteria((prev) => [...prev, { key: '', direction: 'DESC' }]);
    };

    const handleRemoveCriteria = (index) => {
        setSortCriteria((prev) => prev.filter((_, i) => i !== index));
    };

    const handleUpdateCriteria = (index, updatedCriterion) => {
        setSortCriteria((prev) =>
            prev.map((criterion, i) => (i === index ? updatedCriterion : criterion))
        );
    };

    // 페이지 변경
    const handlePageChange = useCallback((page) => {
        if (page >= 0 && page < totalPages) setCurrentPage(page);
    }, [totalPages]);

    const handleNextPageGroup = () => {
        if ((currentPage + 1) < totalPages) setCurrentPage((prev) => prev + 1);
    };

    const handlePrevPageGroup = () => {
        if (currentPage > 0) setCurrentPage((prev) => prev - 1);
    };

    // 메시지 보내기 창 열기
    const handleSendMessage = async (recipientId, message) => {
        console.log(`Sending message to ${recipientId}: ${message}`);
        try {
            const response = await axiosInstance.post(`${process.env.REACT_APP_BACKEND_URL}/chat/start`, {
                recipientId: recipientId,
                message: message
            });
            const roomId = response.data;

            await axiosInstance.post(`${process.env.REACT_APP_BACKEND_URL}/chat/${roomId}`, {
                recipientId: recipientId,
                message: message
            });

            console.log('Message sent, roomId:', roomId);
        } catch (error) {
            console.log('recipientId: ', recipientId);
            console.error('Failed to send message:', error);
        }
    };

    return (
        <div className="tutee-post-list-container">
            <SortButtonGroup
                sortCriteria={sortCriteria}
                onAddCriteria={handleAddCriteria}
                onRemoveCriteria={handleRemoveCriteria}
                onUpdateCriteria={handleUpdateCriteria}
            />
            <div className="title-container">
                <h1 className="tutee-post-list-title">학생 목록</h1>
                {memberRole === 'TUTEE' && (
                    <Link to="/posting-form" className="create-post-button">과외 구인 글 작성하기</Link>
                )}
            </div>
            <div className='filters-container'>
                <h3>필터</h3>
                <div className='filter-group'>
                    <select onChange={(e) => handleFilterChange('gender', e.target.value)}>
                        <option value="">성별 선택</option>
                        <option value="MALE">남성</option>
                        <option value="FEMALE">여성</option>
                    </select>
                    <select onChange={(e) => handleFilterChange('tuteeGradeType', e.target.value)}>
                        <option value="">학년 선택</option>
                        <option value="ELEMENTARY">초등학교</option>
                        <option value="MIDDLE">중학교</option>
                        <option value="HIGH">고등학교</option>
                    </select>
                    <select onChange={(e) => handleFilterChange('tutoringType', e.target.value)}>
                        <option value="">수업 형태 선택</option>
                        <option value="ON_LINE">온라인</option>
                        <option value="OFF_LINE">오프라인</option>
                    </select>
                </div>
            </div>
            <div className="post-grid">
                {postings.map((posting) => (
                    <div key={posting.postingId} className="post-item">
                        <h3>{posting.studentGrade} - {posting.studentSchool}</h3>
                        <p>{posting.personality}</p>
                        <p>Possible Schedule: {posting.possibleSchedule}</p>
                        <p>Level: {posting.level}</p>
                        <p>Fee: {posting.fee}</p>
                        <div className="post-actions">
                            <InquiryButton
                                recipientId={posting.memberId}
                                memberNickname={posting.memberNickname}
                                onSendMessage={handleSendMessage}
                            />
                            <button
                                onClick={() => navigate(`/tutee/post/${posting.postingId}`)}
                                className="details-button"
                            >
                                상세보기
                            </button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="pagination">
                <button onClick={handlePrevPageGroup} disabled={currentPage === 0}>&laquo;</button>
                {Array.from({ length: Math.min(maxPageDisplay, totalPages - currentPage) }, (_, index) => {
                    const page = currentPage + index;
                    return (
                        <button
                            key={page}
                            onClick={() => handlePageChange(page)}
                            className={`page-button ${page === currentPage ? 'active' : ''}`}
                        >
                            {page + 1}
                        </button>
                    );
                })}
                <button onClick={handleNextPageGroup} disabled={currentPage + 1 >= totalPages}>&raquo;</button>
            </div>
        </div>
    );
};

export default TuteePostList;
