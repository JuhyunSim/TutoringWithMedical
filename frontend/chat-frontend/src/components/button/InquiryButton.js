import React, { useState } from 'react';
import MessageModal from '../modal/MessageModal';
import axios from 'axios'; // 서버 API 호출을 위해 axios 사용
import './InquiryButton.css';

const InquiryButton = ({ recipientId, memberNickname }) => {
    const [isModalOpen, setModalOpen] = useState(false);

    const handleOpenModal = () => {
        setModalOpen(true);
    };

    const handleCloseModal = () => {
        setModalOpen(false);
    };

    const handleSendMessage = async (recipientId, message) => {
        try {
            // 서버에 채팅방 생성 및 메시지 전송 요청
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/chatroom/create`, {
                recipientId,
                message,
            });

            if (response.status === 201) {
                alert('채팅방이 생성되었습니다.');
            } else {
                alert('메시지 전송에 실패했습니다.');
            }
        } catch (error) {
            console.error('메시지 전송 실패:', error);
            alert('메시지 전송 중 오류가 발생했습니다.');
        } finally {
            setModalOpen(false); // 메시지 전송 후 모달 닫기
        }
    };

    return (
        <>
            <button className="inquiry-button" onClick={handleOpenModal}>
                과외 문의하기
            </button>
            {isModalOpen && (
                <MessageModal
                    recipientId={recipientId}
                    memberNickname={memberNickname}
                    onSend={handleSendMessage}
                    onClose={handleCloseModal}
                />
            )}
        </>
    );
};

export default InquiryButton;
