import React from 'react';
import Modal from './Modal';

const SubjectFilterModal = ({ isOpen, onClose, subjects, selectedSubjects, onChange }) => {
    const handleSelection = (value) => {
        onChange(selectedSubjects.includes(value)
            ? selectedSubjects.filter((v) => v !== value)
            : [...selectedSubjects, value]);
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="과목 선택">
            <div>
                {subjects.map((subject) => (
                    <label key={subject}>
                        <input
                            type="checkbox"
                            value={subject}
                            checked={selectedSubjects.includes(subject)}
                            onChange={() => handleSelection(subject)}
                        />
                        {subject}
                    </label>
                ))}
            </div>
            <button onClick={onClose} style={{ marginTop: '20px' }}>필터 적용</button>
        </Modal>
    );
};

export default SubjectFilterModal;
