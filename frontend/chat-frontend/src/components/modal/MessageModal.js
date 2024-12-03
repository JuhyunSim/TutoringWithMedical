import React, { useState } from 'react';
import './MessageModal.css'; // 모달 스타일 파일

const MessageModal = ({ recipientId, memberNickname, onClose, onSend }) => {
    const [message, setMessage] = useState('');

    const handleSendMessage = () => {
        if (message.trim()) {
            onSend(recipientId, message); // 부모 컴포넌트에서 처리
            onClose(); // 모달 닫기
        } else {
            alert('메시지를 입력하세요');
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-container">
                <h2>To: {memberNickname}</h2>
                <textarea
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="메시지를 입력하세요"
                    className="modal-input"
                />
                <div className="modal-actions">
                    <button onClick={handleSendMessage} className="send-button">
                        메시지 전송
                    </button>
                    <button onClick={onClose} className="cancel-button">
                        취소
                    </button>
                </div>
            </div>
        </div>
    );
};

export default MessageModal;
