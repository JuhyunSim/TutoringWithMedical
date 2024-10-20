import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TuteePostList from './components/TuteePostList';
import ChatRoom from './components/ChatRoom';
import ChatRoomList from './components/ChatRoomList';
import './App.css'

const App = () => {
    const [roomId, setRoomId] = useState(null);
    const [recipientId, setRecipientId] = useState(null);  // recipientId 저장
    const [memberId, setMemberId] = useState(2);  // 현재 로그인된 튜터 ID (예시)
    const [memberNickname, setMemberNickname] = useState('Tutor1');
    const [memberRole, setMemberRole] = useState('TUTOR');

    const handleRoomSelect = (selectedRoomId, selectedRecipientId) => {
        setRoomId(selectedRoomId)
    }

    return (
        <Router>
            <div>
                {/* With Medical App을 클릭하면 / 경로로 이동 */}
                <Link to="/" className='linkStyle'>
                    <h1>With Medical App</h1>
                </Link>
                {/* 네비게이션 링크 추가 */}
                <nav>
                    <ul>
                        <li><Link to="/posts" className='linkStyle'>게시물 목록</Link></li>
                        <li><Link to="/chatrooms" className='linkStyle'>채팅방 목록</Link></li>
                    </ul>
                </nav>

                {/* 라우팅 설정 */}
                <Routes>
                    {/* 게시물 목록 페이지 */}
                    <Route
                        path="/posts"
                        element={<TuteePostList memberId={memberId}/>}
                    />

                    {/* 채팅방 목록 페이지 */}
                    <Route
                        path="/chatrooms"
                        element={<ChatRoomList memberId={memberId}/>}
                    />

                    {/* 특정 채팅방 페이지 */}
                    <Route
                        path="/chatrooms/:roomId"
                        element={<ChatRoom memberId={memberId} memberNickname={memberNickname} memberRole={memberRole} recipientId={recipientId}/>}
                    />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
