import React, { useState } from 'react';
import MessageModal from '../modal/MessageModal';

const InquiryButton = ({ recipientId, memberNickname, onSendMessage }) => {
    const [isModalOpen, setModalOpen] = useState(false);
    
    const handleOpenModal = () => {
        setModalOpen(true);
    };

    const handleCloseModal = () => {
        setModalOpen(false);
    };

    const handleSendMessage = (recipientId, message) => {
        onSendMessage(recipientId, message); // 상위 컴포넌트로 메시지 전달
        setModalOpen(false); // 메시지 전송 후 모달 닫기
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
