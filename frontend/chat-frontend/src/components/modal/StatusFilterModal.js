import React from 'react';
import Modal from './Modal';

const StatusFilterModal = ({ isOpen, onClose, statuses, selectedStatuses, onChange }) => {
    const handleSelection = (value) => {
        onChange(selectedStatuses.includes(value)
            ? selectedStatuses.filter((v) => v !== value)
            : [...selectedStatuses, value]);
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="등록 상태 선택">
            <div>
                {statuses.map((status) => (
                    <label key={status}>
                        <input
                            type="checkbox"
                            value={status}
                            checked={selectedStatuses.includes(status)}
                            onChange={() => handleSelection(status)}
                        />
                        {status}
                    </label>
                ))}
            </div>
            <button onClick={onClose} style={{ marginTop: '20px' }}>필터 적용</button>
        </Modal>
    );
};

export default StatusFilterModal;
