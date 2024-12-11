import React from 'react';
import Modal from './Modal';

const LocationFilterModal = ({ isOpen, onClose, locations, selectedLocations, onChange }) => {
    const handleSelection = (value) => {
        onChange(selectedLocations.includes(value)
            ? selectedLocations.filter((v) => v !== value)
            : [...selectedLocations, value]);
    };

    return (
        <Modal isOpen={isOpen} onClose={onClose} title="지역 선택">
            <div>
                {locations.map((location) => (
                    <label key={location}>
                        <input
                            type="checkbox"
                            value={location}
                            checked={selectedLocations.includes(location)}
                            onChange={() => handleSelection(location)}
                        />
                        {location}
                    </label>
                ))}
            </div>
            <button onClick={onClose} style={{ marginTop: '20px' }}>필터 적용</button>
        </Modal>
    );
};

export default LocationFilterModal;
