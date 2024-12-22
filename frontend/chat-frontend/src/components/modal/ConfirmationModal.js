import React from 'react';
import './ConfirmationModal.css'; // 스타일 파일 추가

const ConfirmationModal = ({ message, onConfirm, onCancel }) => {
    const formattedMessage = message
        .replace(/\\n/g, "\n") // \\n을 \n으로 변환
        .split("\n") // 줄바꿈 문자로 분리
        .map((line, index) => (
            <span key={index}>
                {line}
                <br />
            </span>
        ));
    console.log("formattedMessage: ", formattedMessage);

    return (
        <div className="modal-overlay">
            <div className="modal-container">
                <p className="modal-message">{formattedMessage}</p>
                <div className="modal-actions">
                    <button className="confirm-button" onClick={onConfirm}>
                        예
                    </button>
                    <button className="cancel-button" onClick={onCancel}>
                        아니오
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmationModal;
