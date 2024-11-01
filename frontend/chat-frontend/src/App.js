import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TuteePostList from './components/tuteePost/TuteePostList';
import ChatRoom from './components/ChatRoom';
import ChatRoomList from './components/ChatRoomList';
import Home from './components/home/Home';
import MyProfile from './components/profile/MyProfile';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';
import './App.css'


const App = () => {
    const [roomId, setRoomId] = useState(null);
    const [recipientId, setRecipientId] = useState(null);  // recipientId 저장
    const [memberId, setMemberId] = useState(2);  // 현재 로그인된 튜터 ID (예시)
    const [memberNickname, setMemberNickname] = useState('Tutor1');
    const [memberRole, setMemberRole] = useState('TUTOR');
    const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리

    const handleLogin = () => setIsLoggedIn(true); // 로그인 성공 시 호출
    const handleLogout = () => setIsLoggedIn(false); // 로그아웃 시 호출

    const handleRoomSelect = (selectedRoomId, selectedRecipientId) => {
        setRoomId(selectedRoomId)
    }

    return (
        <Router>
            <div>
                <header className="fixed-header">
                    <Link to="/" className="linkStyle">
                        <h1>With Medical</h1>
                    </Link>
                    <div className="auth-buttons">
                        {isLoggedIn ? (
                            <>
                                <Link to="/profile" className="auth-link">내 정보</Link>
                                <Link to="/chat" className="auth-link">1:1채팅</Link>
                                <button onClick={handleLogout} className="auth-link">로그아웃</button>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="auth-link">로그인</Link>
                                <Link to="/signup" className="auth-link">회원가입</Link>
                            </>
                        )}
                    </div>
                </header>
                <div className="main-content">
                    {/* 라우팅 설정 */}
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login onLogin={handleLogin} />} />
                        <Route path="/signup" element={<Signup />} />
                        <Route path="/profile" element={<MyProfile />} />
                        <Route path="/chatrooms" element={<ChatRoomList />} />

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
            </div>
        </Router>
    );
};

export default App;
