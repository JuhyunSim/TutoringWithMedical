import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';

const Sidebar = () => {
    return (
        <nav className="sidebar">
            <ul>
                <li>
                    <Link to="/profile/my-posts">나의 게시물</Link>
                </li>
                <li>
                    <Link to="/profile/profile-info">프로필</Link>
                </li>
                <li>
                    <Link to="/profile/chatrooms">채팅방 목록</Link>
                </li>
                <li>
                    <Link to="/profile/delete-account">회원 탈퇴</Link>
                </li>
                <li>
                    <Link to="/profile/change-password">비밀번호 변경</Link>
                </li>
            </ul>
        </nav>
    );
};

export default Sidebar;
