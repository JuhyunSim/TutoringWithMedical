import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { eventWrapper } from '@testing-library/user-event/dist/utils';

const TuteePostList = ({memberId}) => {
    const [postings, setPostings] = useState([]);
    const [selectedPosting, setSelectedPosting] = useState(null); //선택된 게시물
    const [message, setMessage] = useState(''); // 입력한 메시지를 저장
    const [showInput, setShowInput] = useState(false); // 메시지 입력창 표시 여부
    const [messageSent, setMessageSent] = useState(false); // 메시지가 전송되었는지 확인


    useEffect(() => {
        //게시물 리스트 불러오기
        axios.get('http://localhost:8080/tutee/postings')
        .then(response =>{
            console.log(response.data);  // 데이터를 콘솔에 출력하여 확인
            setPostings(response.data.content);})
        .catch(error => console.error('Failed to fetch postings:', error));
    }, []);

    const handleSelectPost = (posting) => {
        setSelectedPosting(posting); //게시물 선택 시, 상세 정보로 이동
        setMessage(''); // 메시지 초기화
        setShowInput(true); // 메시지 입력창 열기
    };

    // 채팅방 생성 및 메시지 전송
    const handleSendMessage = (posting) => {
        const newWindow = window.open('', '', 'width=400,height=300'); // 팝업 창 생성
        newWindow.document.write(`<h2>To: ${posting.memberNickname}</h2>  <!-- 작성자 닉네임으로 제목 표시 -->
            <input id="messageInput" type="text" placeholder="메시지를 입력하세요" style="width: 100%; padding: 10px; margin: 10px 0;" />
            <button id="sendMessageBtn" style="padding: 10px; width: 100%;">메시지 전송</button>
            <script>
                const messageInput = document.getElementById('messageInput');
                const sendMessageBtn = document.getElementById('sendMessageBtn');

                sendMessageBtn.addEventListener('click', () => {
                    const message = messageInput.value;
                    if (message.trim()) {
                        window.opener.postMessage({
                            senderId: ${memberId},  // 현재 로그인한 사용자의 ID
                            recipientId: ${posting.memberId},  // 게시물 작성자의 ID
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
        if (!messageSent) { // 메시지가 이미 전송되었다면 중복 요청 방지
            const { senderId, recipientId, message } = event.data;
            try {
                // 채팅방 생성 API 호출
                const response = await axios.post('http://localhost:8080/chat/start', {
                    senderId: senderId,  
                    recipientId: recipientId,
                    message: message
                });
                const roomId = response.data;  // 생성된 채팅방 ID

                // 채팅방이 생성되면 메시지 전송
                await axios.post(`http://localhost:8080/chat/${roomId}`, {
                    senderId: senderId,
                    recipientId: recipientId,
                    message: message
                });

                console.log('Message sent, roomId:', roomId);
                setMessageSent(true); // 메시지가 전송되었다는 상태 설정ㅊ
            } catch (error) {
                console.error('Failed to send message:', error);
            }
        }
    };
    // 메시지 이벤트 리스너 등록 (중복 등록 방지)
    window.addEventListener('message', handleMessage)

    // 클린업 함수 (이벤트 리스너 제거)
        return () => {
            window.removeEventListener('message', handleMessage);
        };
    }, [memberId]);

    // 엔터 키로 메시지 전송
    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            handleSendMessage(selectedPosting);
        }
    };

    return (
        <div>
            <h1>Tutee Postings</h1>
            {/* 게시물 목록 출력 */}
            {postings.length > 0 && postings.map(posting => (
                <div key={posting.postingId}>
                    <h3>{posting.studentGrade} - {posting.studentSchool}</h3>
                    <p>Personality: {posting.personality}</p>
                    <p>Possible Schedule: {posting.possibleSchedule}</p>
                    <p>Level: {posting.level}</p>
                    <p>Fee: {posting.fee}</p>
                    <button onClick={() => handleSendMessage(posting)}>과외 문의하기</button>
                </div>
            ))}
            
            {/* 게시물이 선택되지 않은 경우 */}
            {postings.length === 0 && <p>게시물이 없습니다.</p>}
        </div>
    );
}

export default TuteePostList;