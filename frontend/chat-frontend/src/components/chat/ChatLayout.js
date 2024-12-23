// ChatLayout.js
import React from 'react';
import Sidebar from '../profile/Sidebar';
import FixedHeader from '../header/FixedHeader';
import { Outlet } from 'react-router-dom';
import './ChatLayout.css';

const ChatLayout = () => {
    return (
        <>
            <FixedHeader />
            <div className="chat-layout">
                <Sidebar />
                <div className="chat-content">
                <Outlet />
                </div>
            </div>
        </>
    );
};

export default ChatLayout;
