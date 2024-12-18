import React, { useState, useEffect } from "react";
import axios from "axios";
import "./SchoolModal.css";

const SchoolModal = ({ onClose, onSave, schoolType }) => {
    const [searchTerm, setSearchTerm] = useState("");
    const [schoolList, setSchoolList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSchools = async () => {
            if (!searchTerm) {
                setSchoolList([]); // 검색어가 없을 경우 목록 초기화
                return;
            }
            setLoading(true);
            setError(null);

            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/schools`, {
                    params: {
                        schoolName: searchTerm,
                        thisPage: 1,
                        perPage: 10,
                        gubun: schoolType === "university" ? "univ_list" : "school_list", // 학교 유형에 따라 API 파라미터 변경
                        schoolType1: schoolType === "university" ? "100323" : "100322", // 대학/학교 구분 값 설정
                        schoolType2: schoolType === "university" ? "100328" : "100329",
                    },
                });
                setSchoolList(response.data.dataSearch?.content || []);
            } catch (err) {
                console.error("Failed to fetch schools:", err);
                setError("학교 정보를 가져오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        const delayDebounceFn = setTimeout(fetchSchools, 300);
        return () => clearTimeout(delayDebounceFn);
    }, [searchTerm, schoolType]);

    const handleSelectSchool = (school) => {
        const selectedSchool = {
            schoolName: school.schoolName,
            seq: school.seq,
        };
        onSave(selectedSchool); // 부모 컴포넌트로 선택 정보 전달
        onClose(); // 모달 닫기
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>{schoolType === "university" ? "대학 검색" : "학교 검색"}</h2>
                <div className="search-bar">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        placeholder={`${schoolType === "university" ? "대학명" : "학교명"}을 입력하세요`}
                    />
                </div>
                {loading && <p>검색 중...</p>}
                {error && <p className="error">{error}</p>}
                <div className="school-list-container">
                    <ul className="school-list">
                        {schoolList.map((school) => (
                            <li
                                key={school.seq}
                                onClick={() => handleSelectSchool(school)}
                                className="school-item"
                            >
                                <p className="school-name">{school.schoolName}</p>
                                <p className="school-info">{school.region} - {school.adres}</p>
                            </li>
                        ))}
                    </ul>
                </div>
                <div className="modal-actions">
                    <button onClick={onClose}>취소</button>
                </div>
            </div>
        </div>
    );
};

export default SchoolModal;
