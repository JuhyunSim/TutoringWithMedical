import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import qs from 'qs'; 
import axiosInstance from '../axios/AxiosInstance';
import './TuteePostList.css';
import { Link } from 'react-router-dom';

const TuteePostList = ({ memberId, memberRole }) => {
    const [postings, setPostings] = useState([]);
    const [selectedPosting, setSelectedPosting] = useState(null);
    const [messageSent, setMessageSent] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;
    const maxPageDisplay = 10;
    const [totalPages, setTotalPages] = useState(0); // API로 받은 총 페이지 수 저장
    const [sortCriteria, setSortCriteria] = useState([]);
    const [sortDirections, setSortDirection] = useState([]);
    const [filters, setFilters] = useState({
        gender: '',
        tuteeGradeType: '',
        tutoringType: ''
    });

    // 게시물 데이터 가져오기
    useEffect(() => {
        const fetchPostings = async () => {
            try {
                const response = await axios.get(
                    `${process.env.REACT_APP_BACKEND_URL}/tutee/postings`,
                    {
                        params: {
                            sortBy: [sortCriteria],
                            sortDirection: sortDirections,
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
    }, [currentPage, sortCriteria, sortDirections, filters]);

    // 메시지 이벤트 리스너 등록
    useEffect(() => {
        if (selectedPosting) {
            const handleMessage = async (event) => {
                if (!messageSent) {
                    const { recipientId, message } = event.data;

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
                        setMessageSent(true);
                    } catch (error) {
                        console.error('Failed to send message:', error);
                    }
                }
            };

            window.addEventListener('message', handleMessage);

            return () => {
                window.removeEventListener('message', handleMessage);
            };
        }
    }, [selectedPosting, messageSent]);

    // 필터 값 변경
    const handleFilterChange = (filterName, value) => {
        setFilters((prevFilters) => ({ ...prevFilters, [filterName]: value }));
    };

    // 정렬 기준 변경
    const handleSortChange = (event) => {
        const selectedValue = event.target.value;
        setSortCriteria((prev) =>
            prev.includes(selectedValue) ? prev.filter((v) => v !== selectedValue) : [...prev, selectedValue]
        );
    };
    

    const handleSortDirectionChange = (event) => {
        setSortDirection(event.target.value);
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
    const handleSendMessage = (posting) => {
        setSelectedPosting(posting);

        const newWindow = window.open('', '', 'width=400,height=300');
        newWindow.document.write(`<h2>To: ${posting.memberNickname}</h2>
            <input id="messageInput" type="text" placeholder="메시지를 입력하세요" style="width: 100%; padding: 10px; margin: 10px 0;" />
            <button id="sendMessageBtn" style="padding: 10px; width: 100%;">메시지 전송</button>
            <script>
                const messageInput = document.getElementById('messageInput');
                const sendMessageBtn = document.getElementById('sendMessageBtn');

                sendMessageBtn.addEventListener('click', () => {
                    const message = messageInput.value;
                    if (message.trim()) {
                        window.opener.postMessage({
                            recipientId: ${posting.memberId},
                            message: message
                        }, '*');
                        window.close();
                    } else {
                        alert('메시지를 입력하세요');
                    }
                });
            </script>`);
    };

    return (
        <div className="tutee-post-list-container">
            <div className="title-container">
                <h1 className="tutee-post-list-title">학생 목록</h1>
                {memberRole === 'TUTEE' && (
                    <Link to="/posting-form" className="create-post-button">과외 구인 글 작성하기</Link>
                )}
            </div>
            <div className='sorting-container'>
                <h3>정렬 기준</h3>
                <div className='sort-group'>
                    <label>
                        <input
                            type="checkbox"
                            value="CREATED_AT"
                            onChange={handleSortChange}
                            checked={sortCriteria.includes("CREATED_AT")}
                        />
                        최신순
                    </label>
                    <label>
                        <input
                            type="checkbox"
                            value="FEE"
                            onChange={handleSortChange}
                            checked={sortCriteria.includes("FEE")}
                        />
                        수업료순
                    </label>
                    <label>
                        <input
                            type="checkbox"
                            value="TUTEE_GRADE"
                            onChange={handleSortChange}
                            checked={sortCriteria.includes("TUTEE_GRADE")}
                        />
                        학년순
                    </label>
                </div>
                {sortCriteria.map((criterion, index) => (
                    <div key={criterion}>
                        <label>{criterion}</label>
                        <select
                            value={sortDirections[index] || "DESC"}
                            onChange={(e) => handleSortDirectionChange(e, criterion)}
                        >
                            <option value="DESC">내림차순</option>
                            <option value="ASC">오름차순</option>
                        </select>
                    </div>
                ))}
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
                            <button
                                onClick={() => handleSendMessage(posting)}
                                className="inquiry-button"
                            >
                                과외 문의하기
                            </button>
                            <button
                                onClick={() => window.location.href = `/tutee/post/${posting.postingId}`}
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
