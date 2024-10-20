import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom'; 
import axios from "axios";

const ChatRoomList = ({memberId}) => {
    const [chatRooms, setChatRooms] = useState([]);
    const navigate = useNavigate();  // 페이지 이동을 위한 훅

    useEffect(() => {
        // 사용자가 생성한 채팅방 목록 조회
        axios.get(`http://localhost:8080/chatrooms?memberId=${memberId}`)  // 템플릿 리터럴 수정
        .then(response => {
            setChatRooms(response.data.content);
        })
        .catch(error => {
            console.error('Failed to fetch chat rooms:', error);
        });
    }, [memberId]);

    const handleRoomSelect = (roomId, recipientId) => {
        navigate(`/chatrooms/${roomId}`, {state: {recipientId}});  // 채팅방으로 이동, recipientId 전달
    };

    return (
        <div>
            <h2>Chat Rooms</h2>
            {chatRooms.length > 0 ? (
                <ul>
                    {chatRooms.map((room) => (
                        <li key={room.roomId}>
                            <button onClick={() => handleRoomSelect(room.roomId, room.recipientId)}>
                                {room.title}
                            </button>
                            <p>Last Message: {room.lastMessage || "No messages yet"}</p>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No chat rooms available.</p>
            )}
        </div>
    );
}; 

export default ChatRoomList;