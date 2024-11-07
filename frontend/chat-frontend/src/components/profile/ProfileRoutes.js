import React from 'react';
import { Route, Routes } from 'react-router-dom';
import MyPosts from './MyPosts';
import ChatRoomList from './ChatRoomList';
import ProfileInfo from './ProfileInfo';
import ChangePassword from './ChangePassword';

const ProfileRoutes = () => (
    <Routes>
        <Route path="my-posts" element={<MyPosts />} />
        <Route path="chatrooms" element={<ChatRoomList />} />
        <Route path="profile-info" element={<ProfileInfo />} />
        <Route path="change-password" element={<ChangePassword />} />
    </Routes>
);

export default ProfileRoutes;
