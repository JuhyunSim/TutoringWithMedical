import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';

const ProfileLayout = () => (
    <div className="profile-layout">
        <Sidebar />
        <main className="profile-content">
            <Outlet />
        </main>
    </div>
);

export default ProfileLayout;
