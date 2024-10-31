import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';

const Login = () => {
    const [step, setStep] = useState(1); // Step 1: Phone number entry, Step 2: Verification code entry
    const [phoneNumber, setPhoneNumber] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    // Step 1: Send verification code to the user's phone
    const handlePhoneNumberSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/send-one?receivePhone=${phoneNumber}`);
            setStep(2); // Move to verification code entry step
        } catch (err) {
            setError('Failed to send verification code. Please try again.');
            console.error(err);
        }
    };

    // Step 2: Verify the entered verification code
    const handleVerificationCodeSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/auth/login/verify?receivePhone=${phoneNumber}&verifyNumber=${verificationCode}`);
            setStep(3); //move to password entry step
        } catch (err) {
            setError('Invalid verification code. Please try again.');
            console.error(err);
        }
    };

    // Step 3: Handle login with password
    const handlePasswordSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await axios.post(`${process.env.REACT_APP_BACKEND_URL}/auth/login`, {
                phoneNumber: phoneNumber,
                password: password

            });
            console.log("Logged in with password:", password);
            // Redirect or further action after successful login
        } catch (err) {
            setError('Failed to log in. Please check your password and try again.');
            console.error(err);
        }
    };

    return (
        <div className="login-container">
            {step === 1 && (
                <>
                    <h2>전화번호 입력</h2>
                    <form onSubmit={handlePhoneNumberSubmit}>
                        <div className="input-group">
                            <label htmlFor="phoneNumber">전화번호</label>
                            <input
                                type="text"
                                id="phoneNumber"
                                value={phoneNumber}
                                onChange={(e) => setPhoneNumber(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="login-button">인증 코드 받기</button>
                    </form>
                </>
            )}
            {step === 2 && (
                <>
                    <h2>인증 코드 입력</h2>
                    <form onSubmit={handleVerificationCodeSubmit}>
                        <div className="input-group">
                            <label htmlFor="verificationCode">인증 코드</label>
                            <input
                                type="text"
                                id="verificationCode"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="login-button">인증번호 확인</button>
                    </form>
                </>
            )}
            {step === 3 && (
                <>
                    <h2>비밀번호 입력</h2>
                    <form onSubmit={handlePasswordSubmit}>
                        <div className="input-group">
                            <label htmlFor="password">비밀번호</label>
                            <input
                                type="password"
                                id="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="login-button">로그인</button>
                    </form>
                </>
            )}
            {error && <p className="error">{error}</p>}
        </div>
    );
};

export default Login;
