// MyProfile.js
import React, { useState } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import './MyProfile.css';

const MyProfile = () => {
    const location = useLocation();
    const [activeTab, setActiveTab] = useState(location.pathname);

    return (
        <div className="profile-container">
            <nav className="sidebar">
                <ul>
                    <li>
                        <Link
                            to="/my-profile/my-posts"
                            className={activeTab === '/my-profile/my-posts' ? 'active' : ''}
                            onClick={() => setActiveTab('/my-profile/my-posts')}
                        >
                            나의 게시물
                        </Link>
                    </li>
                    <li>
                        <Link
                            to="profile-info"
                            className={activeTab === '/my-profile/profile-info' ? 'active' : ''}
                            onClick={() => setActiveTab('/my-profile/profile-info')}
                        >
                            프로필
                        </Link>
                    </li>
                    <li>
                        <Link
                            to="chatrooms"
                            className={activeTab === '/my-profile/chatrooms' ? 'active' : ''}
                            onClick={() => setActiveTab('/my-profile/chatrooms')}
                        >
                            채팅방 목록
                        </Link>
                    </li>
                    <li>
                        <Link
                            to="delete-account"
                            className={activeTab === '/my-profile/delete-account' ? 'active' : ''}
                            onClick={() => setActiveTab('/my-profile/delete-account')}
                        >
                            회원 탈퇴
                        </Link>
                    </li>
                    <li>
                        <Link
                            to="change-password"
                            className={activeTab === '/my-profile/change-password' ? 'active' : ''}
                            onClick={() => setActiveTab('/my-profile/change-password')}
                        >
                            비밀번호 변경
                        </Link>
                    </li>
                </ul>
            </nav>
            <div className="content">
                <Outlet /> {/* 하위 경로에 해당하는 컴포넌트가 여기에 렌더링됩니다 */}
            </div>
        </div>
    );
};

export default MyProfile;
