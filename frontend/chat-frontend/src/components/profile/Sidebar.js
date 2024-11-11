import React from 'react';
import { Link } from 'react-router-dom';
import './Sidebar.css';

const Sidebar = () => {
    return (
        <nav className="sidebar">
            <ul>
                <li>
                    <Link to="/me/my-posts">나의 게시물</Link>
                </li>
                <li>
                    <Link to="/me/profile">프로필</Link>
                </li>
                <li>
                    <Link to="/me/chatrooms">채팅방 목록</Link>
                </li>
                <li>
                    <Link to="/me/delete-account">회원 탈퇴</Link>
                </li>
                <li>
                    <Link to="/me/change-password">비밀번호 변경</Link>
                </li>
            </ul>
        </nav>
    );
};

export default Sidebar;
