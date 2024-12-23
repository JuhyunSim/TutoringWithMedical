import React, { useState, useEffect } from 'react';
import MessageModal from '../modal/MessageModal';
import axios from '../axios/AxiosInstance';
import { useNavigate } from 'react-router-dom';
import ConfirmationModal from '../modal/ConfirmationModal';
import './InquiryButton.css';

const InquiryButton = ({ recipientId, memberNickname }) => {
    const navigate = useNavigate();
    const [isMessageModalOpen, setMessageModalOpen] = useState(false);
    const [isConfirmationModalOpen, setConfirmationModalOpen] = useState(false);
    const [roomId, setRoomId] = useState(null);    
    const [confirmMessage, setConfirmMessage] = useState(""); // 모달 메시지

    useEffect(() => {
        if (roomId) {
            console.log("roomId: ", roomId);
            navigate(`/me/chatrooms/${roomId}`);
        }
    }, [roomId, navigate]);


    const handleOpenMessageModal = async () => {

        try {
            const response = await axios.get(`/chatrooms/exist`, {
                params: { recipientId },
            });
            
            const chatRoomExists = response.data;

            if (chatRoomExists) {
                setConfirmMessage(`이미 ${memberNickname}님과의 대화가 있습니다. 메세지를 전송하시겠습니까?`);
                setRoomId(response.data.roomId);
                setConfirmationModalOpen(true);
            } else {
                setMessageModalOpen(true);
            }

        } catch (error) {
            console.error('중복 체크 실패:', error);
            alert('중복 체크 중 오류가 발생했습니다.');
        }
    };

    const handleCloseMessageModal = () => {
        setMessageModalOpen(false);
    };

    const handleSendMessage = async (recipientId, message) => {
        try {
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/chat/start-and-send`, {
                recipientId,
                message,
            });
            
            if (response.status === 200) {
                setRoomId(response.data.roomId)
                setConfirmMessage("메세지가 전송되었습니다.\n채팅방으로 이동하시겠습니까?");
                setConfirmationModalOpen(true);
            } else {
                alert('메시지 전송에 실패했습니다.');
            }
        } catch (error) {
            console.error('메시지 전송 실패:', error);
            alert('메시지 전송 중 오류가 발생했습니다.');
        } finally {
            setMessageModalOpen(false); // 메시지 전송 후 모달 닫기
        }
    };

    const handleConfirmNavigation = () => {
        if (roomId) {
            navigate(`/chatrooms/${roomId}`); // 채팅방으로 이동
        }
        setConfirmationModalOpen(false);
    };

    const handleCancelNavigation = () => {
        setConfirmationModalOpen(false); // 모달 닫기
    };

    return (
        <>
            <button className="inquiry-button" onClick={handleOpenMessageModal}>
                과외 문의하기
            </button>
            {isMessageModalOpen && (
                <MessageModal
                    recipientId={recipientId}
                    memberNickname={memberNickname}
                    onSend={handleSendMessage}
                    onClose={handleCloseMessageModal}
                />
            )}
            {isConfirmationModalOpen && (
                <ConfirmationModal
                    message={confirmMessage}
                    onConfirm={handleConfirmNavigation}
                    onCancel={handleCancelNavigation}
                />
            )}
        </>
    );
};

export default InquiryButton;
