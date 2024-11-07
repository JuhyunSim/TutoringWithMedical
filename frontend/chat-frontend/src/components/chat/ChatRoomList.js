import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom'; 
import axios from "../axios/AxiosInstance";
import './ChatRoomList.css';

const filterTypes = [
    { label: "전체", value: "ALL" },
    { label: "요청한 대화", value: "CREATED_BY_ME" },
    { label: "요청받은 대화", value: "INVITED" }
];

const ChatRoomList = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [selectedFilter, setSelectedFilter] = useState("ALL");
    const navigate = useNavigate();

    useEffect(() => {
        // 사용자가 생성한 채팅방 목록 조회
        const fetchChatRooms = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/chatrooms`, {
                    params: {
                        filterType: selectedFilter, // 선택된 필터를 쿼리 파라미터로 전달
                        page: 0,
                        size: 10
                    },
                });
                setChatRooms(response.data.content);
                console.log("Fetched Chat Rooms: ", response.data.content);
            } catch(error) {
                console.error('Failed to fetch chat rooms:', error);
            };
        }
        fetchChatRooms();
    }, [selectedFilter]);

    const handleFilterChange = (event) => {
        setSelectedFilter(event.target.value);  // 선택된 필터 업데이트
    };

    const handleRoomSelect = (roomId, recipientId) => {
        navigate(`/chatrooms/${roomId}`, {state: {recipientId}});  // 채팅방으로 이동, recipientId 전달
    };

    const handleRightClick = (event, roomId) => {
        event.preventDefault();
        if (window.confirm("대화를 종료하시겠습니까?")) {
            axios.delete(`${process.env.REACT_APP_BACKEND_URL}/chatrooms/${roomId}`)
                .then(() => {
                    alert("채팅방에서 퇴장했습니다.");
                    setChatRooms((prevRooms) => prevRooms.filter((room) => room.roomId !== roomId));
                })
                .catch(error => console.error("Failed to exit chat room:", error));
        }
    };

    return (
        <div className="chatroom-list-container">
            <h2 className="chatroom-list-header">Chat Rooms</h2>

            {/* 필터 선택 */}
            <div className="filter-container">
                <label htmlFor="filter-select">Filter:</label>
                <select
                    id="filter-select"
                    className="filter-select"
                    value={selectedFilter}
                    onChange={handleFilterChange}
                >
                    {filterTypes.map((filter) => (
                        <option key={filter.value} value={filter.value}>
                            {filter.label}
                        </option>
                    ))}
                </select>
            </div>
            
            {/* 채팅방 목록 */}
            {chatRooms.length > 0 ? (
                <ul className="chatroom-list">
                {chatRooms.map((room) => (
                    <li 
                        key={room.roomId}
                        className="chatroom-item"
                        onClick={(e) => handleRoomSelect(room.roomId, room.recipientId)}
                        onContextMenu={(e) => handleRightClick(e, room.roomId)}
                    >
                        <span className="chatroom-title">{room.title}</span>
                        <span className="last-message"> {room.lastMessage || "No messages yet"}</span>
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