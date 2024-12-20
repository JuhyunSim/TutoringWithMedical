import React from 'react';
import Modal from './Modal';

const UniversityFilterModal = ({ isOpen, onClose, universities, selectedUniversities, onChange }) => {
    const handleSelection = (value) => {
        onChange(
            selectedUniversities.includes(value)
                ? selectedUniversities.filter((v) => v !== value)
                : [...selectedUniversities, value]
        );
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="대학 선택">
            <div>
                {universities.map((university) => (
                    <label key={university}>
                        <input
                            type="checkbox"
                            value={university}
                            checked={selectedUniversities.includes(university)}
                            onChange={() => handleSelection(university)}
                        />
                        {university}
                    </label>
                ))}
            </div>
            <button onClick={onClose} style={{ marginTop: '20px' }}>
                필터 적용
            </button>
        </Modal>
    );
};

export default UniversityFilterModal;