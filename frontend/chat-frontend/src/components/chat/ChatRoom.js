import React, { useEffect, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './ChatRoom.css'

const ChatRoom = ({ memberId, recipientId, memberNickname }) => {
    const { roomId } = useParams();
    const [messages, setMessages] = useState([]);  // 채팅 메시지
    const [inputMessage, setInputMessage] = useState('');  // 입력 메시지
    const [isConnected, setIsConnected] = useState(false);  // 웹소켓 연결 여부
    const [page, setPage] = useState(0);  // 페이지 번호
    const [hasMore, setHasMore] = useState(true);  // 추가 메시지 여부
    const messageEndRef = useRef(null);  // 스크롤 끝 참조
    const stompClient = useRef(null);
    const [isComposing, setIsComposing] = useState(false);  // 한글 입력 여부
    const containerRef = useRef(null);  // 스크롤 컨테이너 참조

    const token = localStorage.getItem('jwtToken'); // JWT 토큰 가져오기
    const bearerToken = `Bearer ${token}`;

    // 처음 메시지를 불러오는 함수 (최신 메시지부터)
    const loadMessages = async () => {
        if (!hasMore) return;  // 더 이상 메시지가 없을 경우 반환

        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/chatrooms/${roomId}/messages`, {
                params: { page, size: 30 },  // 페이지와 사이즈 지정
            });

            const newData = response.data.content.reverse();  // 불러온 데이터 뒤집기 (오름차순으로 표시)

            // 새로운 메시지 추가 (기존 메시지 유지)
            setMessages((prevMessages) => [...newData, ...prevMessages]);
            setHasMore(!response.data.last);  // 마지막 페이지 여부 설정

        } catch (error) {
            console.error("Error loading messages:", error);
        }
    };

    // 스크롤이 맨 위에 도달하면 이전 페이지의 메시지를 불러오는 함수
    const handleScroll = () => {
        if (containerRef.current.scrollTop === 0 && hasMore) {
            setPage((prevPage) => prevPage + 1);  // 페이지 증가
        }
    };

    useEffect(() => {
        loadMessages();  // 페이지가 변경될 때마다 메시지를 불러옴
    }, [page]);

    useEffect(() => {
        if (containerRef.current) {
            containerRef.current.scrollTop = containerRef.current.scrollHeight;
        }
    }, [messages]);

    useEffect(() => {
        if (roomId && !stompClient.current) {
            const socket = new SockJS(`${process.env.REACT_APP_BACKEND_URL}/ws-stomp?token=${bearerToken}`);
            const client = new Client({
                webSocketFactory: () => socket,
                debug: (str) => console.log(str),
                onConnect: () => {
                    console.log('Connected to WebSocket server');
                    setIsConnected(true);
                    client.subscribe(`/topic/chat/${roomId}`, (msg) => {
                        const message = JSON.parse(msg.body);
                        setMessages((prevMessages) => [...prevMessages, message]);  // 새로운 메시지는 마지막에 추가
                    });
                },
                onStompError: (frame) => console.error('STOMP Error:', frame.headers['message']),
                onDisconnect: () => {
                    notifyExit();
                    setIsConnected(false);
                }
            });

            client.activate();
            stompClient.current = client;

            return () => {
                if (stompClient.current) {
                    notifyExit();
                    stompClient.current.deactivate();
                    stompClient.current = null;
                }
            };
        }
    }, [roomId, token]);

    // 메시지 전송 함수
    const sendMessage = () => {
        if (stompClient.current && isConnected && inputMessage.trim() !== '') {
            const message = {
                message: inputMessage,
            };
            stompClient.current.publish({
                destination: `/app/chat/${roomId}`,
                body: JSON.stringify(message),
            });
            setInputMessage('');  // 입력 필드 초기화
        } else {
            console.error('STOMP client is not connected or message is empty');
        }
    };

    // 엔터 키를 눌렀을 때 메시지 전송
    const handleKeyPress = (event) => {
        if (event.key === 'Enter' && !isComposing) {
            event.preventDefault();
            sendMessage();
        }
    };

    const handleCompositionStart = () => setIsComposing(true);
    const handleCompositionEnd = () => setIsComposing(false);

    const notifyExit = () => {
        if (stompClient.current && isConnected) {
            const exitMessage = `${memberNickname}님이 대화를 종료하였습니다.`;
            stompClient.current.publish({
                destination: `/app/chat/${roomId}`,
                body: JSON.stringify({ message: exitMessage })
            });
        }
    };

    return (
        <div className="chatroom-container" ref={containerRef}>
            <h2>Chat Room: {roomId}</h2>
            <div className="chatroom-messages">

                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.senderNickname}:</strong> {msg.message}
                    </div>
                ))}
                <div ref={messageEndRef} />
            </div>


            <div className="chatroom-input">
                <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    onCompositionStart={handleCompositionStart}
                    onKeyDown={handleKeyPress}
                    onCompositionEnd={handleCompositionEnd}
                    placeholder="메시지를 입력하세요"
                    style={{ flex: 1, padding: '10px' }}
                />
                <button onClick={sendMessage} style={{ padding: '10px' }}>메시지 전송</button>
            </div>
        </div>
    );
};

export default ChatRoom;
