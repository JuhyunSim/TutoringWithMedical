// ChatLayout.js
import React from 'react';
import Sidebar from '../profile/Sidebar';
import { Outlet } from 'react-router-dom';
import './ChatLayout.css';

const ChatLayout = () => {
    return (
        <div className="chat-layout">
            <Sidebar />
            <div className="chat-content">
                <Outlet />
            </div>
        </div>
    );
};

export default ChatLayout;
