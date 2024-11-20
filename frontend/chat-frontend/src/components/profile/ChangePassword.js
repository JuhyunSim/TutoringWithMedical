import React, { useState } from 'react';
import axios from '../axios/AxiosInstance';
import './ChangePassword.css';

const ChangePassword = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleChangePassword = async () => {
        if (!oldPassword || !newPassword || !confirmPassword) {
            setErrorMessage('모든 항목을 입력해주세요.');
            return;
        }
        if (newPassword !== confirmPassword) {
            setErrorMessage('새 비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            await axios.patch(`${process.env.REACT_APP_BACKEND_URL}/auth/me/password`, {
                oldPassword,
                newPassword,
                confirmPassword
            });
            alert('비밀번호가 성공적으로 변경되었습니다.');
            window.location.href = '/me/profile';
        } catch (error) {
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data.message || '비밀번호 변경에 실패했습니다.');
            } else {
                setErrorMessage('서버와 통신 중 오류가 발생했습니다.');
            }
        }
    };

    return (
        <div className="change-password">
            <h2>비밀번호 변경</h2>
            <p>안전한 비밀번호로 내 정보를 보호하세요.<br />다른 사이트에서 사용하지 않은 비밀번호를 권장합니다.</p>
            <input
                type="password"
                placeholder="현재 비밀번호"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
            />
            <input
                type="password"
                placeholder="새 비밀번호"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
            />
            <input
                type="password"
                placeholder="새 비밀번호 확인"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
            />
            <button onClick={handleChangePassword}>확인</button>
            <button onClick={() => (window.location.href = '/me/profile')}>취소</button>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
        </div>
    );
};

export default ChangePassword;
