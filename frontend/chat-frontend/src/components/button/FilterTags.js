import React from 'react';
import './FilterTags.css';

const FilterTags = ({ filters, onRemove }) => {
    // 각 필터 리스트를 렌더링
    const renderTags = () => {
        const tags = [];

        Object.keys(filters).forEach((key) => {
            if (Array.isArray(filters[key])) { // filters[key]가 배열인지 확인
                filters[key].forEach((value, index) => {
                    tags.push(
                        <div className="filter-tag" key={`${key}-${index}`}>
                            {value}
                            <button className="remove-button" onClick={() => onRemove(key, value)}>
                                ×
                            </button>
                        </div>
                    );
                });            
            }
        });

        return tags;
    };

    return <div className="filter-tags-container">{renderTags()}</div>;
};

export default FilterTags;
