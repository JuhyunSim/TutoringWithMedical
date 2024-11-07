import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TuteePostList from './components/tuteePost/TuteePostList';
import ChatRoom from './components/chat/ChatRoom';
import ChatRoomList from './components/chat/ChatRoomList';
import Home from './components/home/Home';
import ProfileLayout from './components/profile/ProfileLayout';
import Login from './components/login/Login';
import Signup from './components/signup/Signup';
import PostingForm from './components/tuteePost/PostingForm';
import MyPosts from './components/profile/MyPosts';
import ChangePassword from './components/profile/ChangePassword';
import DeleteAccount from './components/profile/DeleteAccount';
import ProfileInfo from './components/profile/ProfileInfo';
import EditTuteePostForm from './components/tuteePost/EditTuteePostForm';
import TutorList from './components/tutor/TutorList';
import TutorProfile from './components/tutor/TutorProfile';
import ChatLayout from './components/chat/ChatLayout';
import './App.css'
import { faker } from '@faker-js/faker';


const App = () => {
    const [roomId, setRoomId] = useState(null);
    const [recipientId, setRecipientId] = useState(null);  // recipientId 저장
    const [memberId, setMemberId] = useState(null);
    const [memberNickname, setMemberNickname] = useState('Tutor1');
    const [memberRole, setMemberRole] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 상태 관리

    const handleLogin = (role) => {
        setIsLoggedIn(true);
        setMemberRole(role);
        setMemberId(memberId);
    } // 로그인 성공 시 호출

    const handleLogout = () => {
        setIsLoggedIn(false); // 로그아웃 시 호출
        setMemberRole(null);
        setMemberId(null)
    }

    const handleRoomSelect = (selectedRoomId, selectedRecipientId) => {
        setRoomId(selectedRoomId);
        setRecipientId(selectedRecipientId);
    }

    return (
        <Router>
            <div>
                <header className="fixed-header">
                    <Link to="/" className="linkStyle">
                        <h1>의대생과 과외하기</h1>
                    </Link>
                    <div className="auth-buttons">
                        {isLoggedIn ? (
                            <>
                                <Link to="/profile" className="auth-link">내 정보</Link>
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
                        <Route path="/posting-form" element={<PostingForm />} /> {/* 게시물 작성 폼 */}
                        
                        {/* MyProfile 내부 라우트 설정 */}
                        <Route path="/profile" element={<ProfileLayout />}>
                            <Route path="my-posts" element={<MyPosts />} />\
                            <Route path="edit-post/:postingId" element={<EditTuteePostForm />} /> {/* 추가된 라우트 */}
                            <Route path="profile-info" element={<ProfileInfo />} />
                            <Route path="chatrooms" element={<ChatRoomList />} />
                            <Route path="delete-account" element={<DeleteAccount />} />
                            <Route path="change-password" element={<ChangePassword />} />
                        </Route>
                        
                        <Route path="/posts" element={<TuteePostList memberId={memberId} memberRole={memberRole}/>}/>
                        <Route path="/tutor-profiles" element={<TutorList />} />
                        <Route path="/tutor-profiles/:tutorId" element={<TutorProfile/>}/>
                        {/* Chat Layout */}
                        <Route path="/chatrooms" element={<ChatLayout />}>
                            <Route path=":roomId" element={<ChatRoom memberId={memberId} memberNickname={memberNickname} memberRole={memberRole} recipientId={recipientId}/>}/>
                        </Route>
                    </Routes>
                </div>
            </div>
        </Router>
    );
};

export default App;
