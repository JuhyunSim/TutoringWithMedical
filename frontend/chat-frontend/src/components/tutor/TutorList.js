import React, { useEffect, useState } from 'react';
import axios from 'axios';
import qs from 'qs';
import './TutorList.css';
import { Link } from 'react-router-dom';
import LocationModal from '../modal/LocationModal';
import StatusFilterModal from '../modal/StatusFilterModal';
import SubjectFilterModal from '../modal/SubjectFilterModal';
import SchoolModal from '../modal/SchoolModal';
import FilterTags from '../button/FilterTags';
import InquiryButton from '../button/InquiryButton';

const TutorList = () => {
    const [tutors, setTutors] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;
    const maxPageDisplay = 10;
    const [totalPages, setTotalPages] = useState(0);

    const [filters, setFilters] = useState({
        gender: '',
        subjects: [],
        locations: [],
        universities: [],
        statusList: [],
    });

    const [enumOptions, setEnumOptions] = useState({
        subjects: [],
        statusList: [],
        gender: [],
    });

    const [modalState, setModalState] = useState({
        location: false,
        subject: false,
        status: false,
        university: false,
    });

    useEffect(() => {
        fetchEnumOptions();
    }, []);

    useEffect(() => {
        fetchTutors();
    }, [currentPage, filters]);

    const fetchEnumOptions = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/tutor/filters`);
            setEnumOptions(response.data);
        } catch (error) {
            console.error('Failed to fetch enum options:', error);
        }
    };

    const fetchTutors = async () => {
        try {
            const response = await axios.get(
                `${process.env.REACT_APP_BACKEND_URL}/tutor`, {
                    params: {
                        gender: filters.gender || null,
                        subjects: filters.subjects.length ? filters.subjects : null,
                        locations: filters.locations.length ? filters.locations : null,
                        universities: filters.universities.length ? filters.universities : null,
                        statusList: filters.statusList.length ? filters.statusList : null,
                        page: currentPage,
                        size: pageSize,
                    },
                    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),
                }
            );
            setTutors(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Failed to fetch tutor profiles:', error);
        }
    };

    const handleFilterChange = (key, value) => {
        setFilters((prev) => ({ ...prev, [key]: value }));
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setCurrentPage(newPage);
        }
    };

    const handleFilterRemove = (key, value) => {
        setFilters((prev) => ({
            ...prev,
            [key]: prev[key].filter((item) => item !== value),
        }));
    };

    return (
        <div className="tutor-list-container">
            <div className="title-container">
                <h1 className="tutor-list-title">선생님 목록</h1>
            </div>

            <FilterTags filters={filters} onRemove={handleFilterRemove} />

            <div className="filters-container">
                <button className='filter-button' onClick={() => setModalState((prev) => ({ ...prev, location: true }))}>
                    지역 선택
                </button>
                <button className='filter-button' onClick={() => setModalState((prev) => ({ ...prev, subject: true }))}>
                    과목 선택
                </button>
                <button className='filter-button' onClick={() => setModalState((prev) => ({ ...prev, university: true }))}>
                    대학 선택
                </button>
                <button className='filter-button' onClick={() => setModalState((prev) => ({ ...prev, status: true }))}>
                    등록 상태 선택
                </button>
            </div>

            {/* 모달들 */}
            {modalState.location && (
            <LocationModal 
                onClose={() => setModalState((prev) => ({ ...prev, location: false }))}
                onSave={( value ) => {
                    const location = `${value.sido.addr_name} ${value.sigungu.addr_name}`; 
                    setFilters((prev) => ({
                        ...prev,
                        locations: [...(prev.locations || []), location],
                    }));
                }}
            />
            )}

            <SubjectFilterModal
                isOpen={modalState.subject}
                onClose={() => setModalState((prev) => ({ ...prev, subject: false }))}
                subjects={enumOptions.subjects}
                selectedSubjects={filters.subjects}
                onChange={(value) => handleFilterChange('subjects', value)}
            />

            <StatusFilterModal
                isOpen={modalState.status}
                onClose={() => setModalState((prev) => ({ ...prev, status: false }))}
                statuses={enumOptions.statusList}
                selectedStatuses={filters.statusList}
                onChange={(value) => handleFilterChange('statusList', value)}
            />

            {modalState.university && (
            <SchoolModal 
                onClose={() => setModalState((prev) => ({ ...prev, university: false }))} 
                onSave={(value) => handleFilterChange('universities', value)} 
                schoolType={"university"}/>
            )}


            {/* 선생님 리스트 */}
            <div className="tutor-grid">
                {tutors.map(tutor => (
                    <div key={tutor.id} className="tutor-item">
                        {tutor.imageUrl ? (
                            <img
                                src={`${process.env.REACT_APP_BACKEND_URL}${tutor.imageUrl}`}
                                alt={`${tutor.nickname}의 프로필 이미지`}
                                className="tutor-image"
                            />
                        ) : (
                            <div className="placeholder-image">이미지 없음</div>
                        )}
                        <h3>{tutor.nickname}</h3>
                        <p>학교: {tutor.univName}</p>
                        <p>지역: {tutor.location}</p>
                        <p>과목: {tutor.subjects.join(', ')}</p>
                        <div className="button-row">
                            <Link to={`/tutor-profiles/${tutor.id}`} className="view-profile-button button-common">프로필 보기</Link>
                            <InquiryButton 
                                recipientId={tutor.id}
                                memberNickname={tutor.nickname}
                            />
                        </div>
                    </div>
                ))}
            </div>
            
            {/* 페이지네이션 */}
            <div className="pagination">
                <button onClick={() => handlePageChange(0)} disabled={currentPage === 0}>처음</button>
                <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0}>&lt;</button>
                {Array.from({ length: Math.min(maxPageDisplay, totalPages - currentPage) }, (_, index) => {
                    const page = currentPage + index;
                    return (
                        <button
                            key={page}
                            onClick={() => handlePageChange(page)}
                            className={`page-button ${page === currentPage ? 'active' : ''}`}
                        >
                            {page + 1}
                        </button>
                    );
                })}
                <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage + 1 >= totalPages}>&gt;</button>
                <button onClick={() => handlePageChange(totalPages - 1)} disabled={currentPage === totalPages - 1}>끝</button>
            </div>
        </div>
    );
};

export default TutorList;
