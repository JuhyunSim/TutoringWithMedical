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
import MainContentWrapper from './components/mainWrapperComponent/MainContentWrapper';
import FixedHeader from './components/header/FixedHeader';
import ProfileEdit from './components/profile/ProfileEdit';
import Membership from './components/memberShip/MemberShip';
import TuteePost from './components/tuteePost/TuteePost';
import { useAuth } from './components/context/AuthProvider';

const AppContent = () => {

    const [recipientId, setRecipientId] = useState(null);  // recipientId 저장
    const [memberId, setMemberId] = useState(null);
    const [memberNickname, setMemberNickname] = useState('Tutor1');
    const [memberRole, setMemberRole] = useState(null);
    const { isLoading } = useAuth();

    if (isLoading) {
        return <div>Loading...</div>; // 초기화 중 표시
    }

    return (
        <>
            <FixedHeader />
            {/* 라우팅 설정 */}
            <Routes>
                <Route path="/" element={<MainContentWrapper><Home /></MainContentWrapper>} />
                <Route path="/login" element={<MainContentWrapper><Login /></MainContentWrapper>} />
                <Route path="/signup" element={<MainContentWrapper><Signup /></MainContentWrapper>} />
                <Route path="/posting-form" element={<MainContentWrapper><PostingForm /></MainContentWrapper>} />
                <Route path="/posts" element={<MainContentWrapper><TuteePostList memberId={memberId} memberRole={memberRole}/></MainContentWrapper>}/>
                <Route path="/tutee/post/:postingId" element={<MainContentWrapper><TuteePost /></MainContentWrapper>} />
                <Route path="/tutor-profiles" element={<MainContentWrapper><TutorList /></MainContentWrapper>} />
                <Route path="/tutor-profiles/:tutorId" element={<MainContentWrapper><TutorProfile/></MainContentWrapper>}/>

                {/* MyProfile 내부 라우트 설정 */}
                <Route path="/me" element={<ProfileLayout />}>
                    <Route path="my-posts" element={<MyPosts />} />\
                    <Route path="edit-post/:postingId" element={<EditTuteePostForm />} /> {/* 추가된 라우트 */}
                    <Route path="profile" element={<ProfileInfo />} />
                    <Route path="profile/edit" element={<ProfileEdit />} />
                    <Route path="chatrooms">
                        <Route index element={<ChatRoomList />} /> {/* 기본 경로 */}
                    </Route>
                    <Route path="delete-account" element={<DeleteAccount />} />
                    <Route path="change-password" element={<ChangePassword />} />
                    <Route path="membership" element={<Membership />} />
                </Route>

                <Route path="/chatrooms" element={<ChatLayout />}>
                    <Route path=":roomId" element={<ChatRoom memberId={memberId} memberNickname={memberNickname} memberRole={memberRole} recipientId={recipientId}/>}/>
                </Route>
            </Routes>
        </>
    );
}

const App = () => {
    return (
        <Router>
            <AppContent />
        </Router>
    );
};

export default App;
