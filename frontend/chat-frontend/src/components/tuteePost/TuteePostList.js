import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { eventWrapper } from '@testing-library/user-event/dist/utils';
import mockPostings from './mockData'; 
import './TuteePostList.css';
import { Link } from 'react-router-dom';


const TuteePostList = ({ memberId, memberRole }) => {
    const [postings, setPostings] = useState([]);
    const [selectedPosting, setSelectedPosting] = useState(null); 
    const [message, setMessage] = useState(''); 
    const [showInput, setShowInput] = useState(false); 
    const [messageSent, setMessageSent] = useState(false); 
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;
    const maxPageDisplay = 10;
    const totalPage = Math.ceil(mockPostings.length / pageSize);
    const startPage = Math.floor(currentPage / maxPageDisplay) * maxPageDisplay;

    useEffect(() => {
        console.log("Member Role:", memberRole);  // 콘솔에서 memberRole 값을 확인
    }, [memberRole]);

    //목데이터에 적용
    useEffect(() => {
        // const startIndex = currentPage * pageSize;
        // const paginatedPostings = mockPostings.slice(startIndex, startIndex + pageSize);
        // setPostings(paginatedPostings);
    }, [currentPage]);

    useEffect(() => {
        // 목데이터 사용을 위해 주석처리 
        axios.get(`${process.env.REACT_APP_BACKEND_URL}/tutee/postings`)
            .then(response => {
                console.log(response.data);  
                setPostings(response.data.content);
            })
            .catch(error => console.error('Failed to fetch postings:', error));
    }, []);

    const handleSelectPost = (posting) => {
        setSelectedPosting(posting); 
        setMessage(''); 
        setShowInput(true); 
    };

    const handlePageChange = (page) => {
        if (page >= 0 && page < totalPage) setCurrentPage(page);
    };

    const handleNextPageGroup = () => {
        if (startPage + maxPageDisplay < totalPage) {
            setCurrentPage(startPage + maxPageDisplay);
        }
    };

    const handlePrevPageGroup = () => {
        if (startPage > 0) {
            setCurrentPage(startPage - maxPageDisplay);
        }
    };

    const handleSendMessage = (posting) => {
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
                            senderId: ${memberId},
                            recipientId: ${posting.memberId},
                            message: message
                        }, '*');
                        window.close();
                    } else {
                        alert('메시지를 입력하세요');
                    }
                });
            </script>`
        );
    };

    useEffect(() => {
        const handleMessage = async (event) => {
            if (!messageSent) { 
                const { senderId, recipientId, message } = event.data;
                try {
                    const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/chat/start`, {
                        senderId: senderId,
                        recipientId: recipientId,
                        message: message
                    });
                    const roomId = response.data; 

                    await axios.post(`${process.env.REACT_APP_BACKEND_URL}/chat/${roomId}`, {
                        senderId: senderId,
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
    }, [memberId]);

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            handleSendMessage(selectedPosting);
        }
    };

    return (
        <div className="tutee-post-list-container">
            <div className="title-container">
                <h1 className="tutee-post-list-title">학생 목록</h1>
                {memberRole === 'TUTEE' && (
                        <Link to="/posting-form" className="create-post-button">과외 구인 글 작성하기</Link>
                    )}
            </div>
            <div className="post-grid">
                {postings.length > 0 && postings.map(posting => (
                <div key={posting.postingId} className="post-item">
                    <h3 className="post-title">{posting.studentGrade} - {posting.studentSchool}</h3>
                    <p className="post-detail">Personality: {posting.personality}</p>
                    <p className="post-detail">Possible Schedule: {posting.possibleSchedule}</p>
                    <p className="post-detail">Level: {posting.level}</p>
                    <p className="post-detail">Fee: {posting.fee}</p>
                    <button onClick={() => handleSendMessage(posting)} className="inquiry-button">과외 문의하기</button>
                </div>
            ))}
            {postings.length === 0 && <p className="no-post-message">게시물이 없습니다.</p>}
            </div>

            {/* 페이지네이션 버튼 */}
            <div className="pagination">
                <button onClick={handlePrevPageGroup} disabled={startPage === 0}>&laquo;</button>
                {Array.from({ length: Math.min(maxPageDisplay, totalPage - startPage) }, (_, index) => {
                    const page = startPage + index;
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
                <button onClick={handleNextPageGroup} disabled={startPage + maxPageDisplay >= totalPage}>&raquo;</button>
            </div>
        </div>
    );
};

export default TuteePostList;
