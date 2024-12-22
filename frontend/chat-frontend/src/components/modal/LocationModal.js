import React, { useState, useEffect } from "react";
import axios from "axios";
import "./LocationModal.css";

const LocationModal = ({ onClose, onSave }) => {
    const [sidoList, setSidoList] = useState([]);
    const [sigunguList, setSigunguList] = useState([]);
    const [selectedSido, setSelectedSido] = useState(null); // 선택된 시/도
    const [selectedSigungu, setSelectedSigungu] = useState(null); // 선택된 시/군/구

    useEffect(() => {
        // Fetch 시도 리스트
        const fetchSido = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/locations`, {
                    params: { cd: "", pg_yn: "0" },
                });
                setSidoList(response.data.result);
            } catch (err) {
                console.error("Failed to fetch 시도 data:", err);
            }
        };
        fetchSido();
    }, []);

    const fetchSigungu = async (sidoCode) => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/locations`, {
                params: { cd: sidoCode, pg_yn: "0" },
            });
            setSigunguList(response.data.result);
        } catch (err) {
            console.error("Failed to fetch 시군구 data:", err);
        }
    };

    const handleSidoSelect = (sido) => {
        setSelectedSido(sido); // 시/도 선택
        fetchSigungu(sido.cd); // 해당 시/도의 시군구 데이터 로드
    };

    const handleSigunguSelect = (sigungu) => {
        setSelectedSigungu(sigungu); // 시/군/구 선택
    };

    const handleSave = () => {
        if (!selectedSigungu) {
            alert("시군구를 선택해주세요!");
            return;
        }
        console.log('sido: ', selectedSido );
        console.log('sigungu: ', selectedSigungu );
        onSave({ 
            sido: selectedSido,
            sigungu: selectedSigungu
         }); // 선택한 지역 전달
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>지역 선택</h2>
                <div className="modal-body">
                    {/* 시/도 목록 */}
                    <div className="sido-list">
                        <h3>시/도</h3>
                        <ul>
                            {sidoList.map((sido) => (
                                <li
                                    key={sido.cd}
                                    onClick={() => handleSidoSelect(sido)}
                                    className={selectedSido?.cd === sido.cd ? "selected" : ""}
                                >
                                    {sido.addr_name}
                                </li>
                            ))}
                        </ul>
                    </div>

                    {/* 시군구 목록 */}
                    <div className="sigungu-list">
                        <h3>시/군/구</h3>
                        {selectedSido ? (
                            <ul>
                                {sigunguList.map((sigungu) => (
                                    <li
                                        key={sigungu.cd}
                                        onClick={() => handleSigunguSelect(sigungu)}
                                        className={selectedSigungu?.cd === sigungu.cd ? "selected" : ""}
                                    >
                                        {sigungu.addr_name}
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>시/도를 먼저 선택해주세요.</p>
                        )}
                    </div>
                </div>
                <div className="modal-actions">
                    <button className="cancel-button" onClick={onClose}>취소</button>
                    <button className="confirm-button" onClick={handleSave} disabled={!selectedSigungu}>
                        저장
                    </button>
                </div>
            </div>
        </div>
    );
};

export default LocationModal;
