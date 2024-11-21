import React, { useState } from 'react';
import axios from '../axios/AxiosInstance';
import './DeleteAccount.css';
console.log('Axios Instance:', axios);

const DeleteAccount = () => {
    const [isChecked, setIsChecked] = useState(false);
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleDeleteAccount = async () => {
        console.log('handleDeleteAccount 함수 호출됨');
        if (!isChecked || !password) {
            setErrorMessage('모든 항목을 완료해주세요.');
            return;
        }

        try {
            await axios.delete(`${process.env.REACT_APP_BACKEND_URL}/auth/me`, {
                data: password,
            });
            alert('회원탈퇴가 완료되었습니다.');
            window.location.href = '/';
        } catch (error) {
            console.log('Axios 요청 실패', error);
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data.message || '회원탈퇴에 실패했습니다. 비밀번호를 확인해주세요.');
            } else {
                setErrorMessage('서버와 통신 중 오류가 발생했습니다.');
            }
        }
    };

    return (
        <div className="delete-account">
            <h2>회원탈퇴</h2>
            <p>회원 탈퇴 시 모든 개인정보와 서비스 이용 관련 데이터가 삭제됩니다.<br />삭제된 데이터는 복구할 수 없습니다.</p>
            <label>
                <input
                    type="checkbox"
                    checked={isChecked}
                    onChange={(e) => setIsChecked(e.target.checked)}
                />
                확인했습니다.
            </label>
            <input
                type="password"
                placeholder="비밀번호를 입력하세요"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button disabled={!isChecked || !password}
                    onClick={() => {
                        handleDeleteAccount()
                        }}
                        >
                회원탈퇴
            </button>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
        </div>
    );
};

export default DeleteAccount;
