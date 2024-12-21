import React from 'react';
import './SortButtonGroup.css';

const SortButtonGroup = ({ 
     sortCriteria,
     onAddCriteria, 
     onRemoveCriteria, 
     onUpdateCriteria,
     maxCriteria = 3 // 최대 정렬 기준 개수 기본값
    }) => {
    const sortOptions = [
        { value: 'CREATED_AT', label: '게시일' },
        { value: 'FEE', label: '수업료' },
        { value: 'TUTEE_GRADE', label: '학년' },
    ];

    const directionOptions = [
        { value: 'ASC', label: '오름차순' },
        { value: 'DESC', label: '내림차순' },
    ];

    return (
        <div className="sort-criteria-group">
            <h3>정렬 기준</h3>
            {sortCriteria.map((criterion, index) => (
                <div key={index} className="sort-criteria-row">
                    <select
                        value={criterion.key}
                        onChange={(e) =>
                            onUpdateCriteria(index, { ...criterion, key: e.target.value })
                        }
                        className="sort-select"
                    >
                        <option value="">정렬 기준 선택</option>
                        {sortOptions.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                    <select
                        value={criterion.direction}
                        onChange={(e) =>
                            onUpdateCriteria(index, { ...criterion, direction: e.target.value })
                        }
                        className="direction-select"
                    >
                        {directionOptions.map((option) => (
                            <option key={option.value} value={option.value}>
                                {option.label}
                            </option>
                        ))}
                    </select>
                    <button
                        onClick={() => onRemoveCriteria(index)}
                        className="remove-criteria-button"
                    >
                        삭제
                    </button>
                </div>
            ))}
            {sortCriteria.length < maxCriteria && (
                <button onClick={onAddCriteria} className="add-criteria-button">
                    기준 추가
                </button>
            )}
        </div>
    );
};

export default SortButtonGroup;
