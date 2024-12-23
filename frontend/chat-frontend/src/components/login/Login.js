import React, { useState } from 'react';
import { useAsyncValue, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthProvider';
import './Login.css';

const Login = () => {
    const [phoneNumber, setPhoneNumber] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState(''); // 추가된 역할 상태
    const [error, setError] = useState('');
    const navigate = useNavigate(); // 리다이렉션을 위한 useNavigate 훅 사용
    const { login } = useAuth();

    // Handle login with phone number and password
    const handleLoginSubmit = async (e) => {
        e.preventDefault();
        if (!role) {
            setError('역할을 선택해 주세요.');
            return;
        }
        try {
            const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/auth/login`, {
                phoneNumber: phoneNumber,
                password: password,
                role: role
            });
            const token = response.data.accessToken;
            localStorage.setItem('jwtToken', token);
            login(role, token)
            console.log("Logged in successfully with phone number and password.");
            navigate('/');
        } catch (err) {
            setError('Failed to log in. Please check your phone number and password and try again.');
            console.error(err);
        }
    };

    return (
        <div className="login-container">
            <h2>로그인</h2>
            <form onSubmit={handleLoginSubmit}>
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
                <div className="role-options">
                    <label>
                        <input
                            type="radio"
                            name="role"
                            value="TUTOR"
                            checked={role === "TUTOR"}
                            onChange={(e) => setRole(e.target.value)}
                            required
                        />
                        Tutor
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="role"
                            value="TUTEE"
                            checked={role === "TUTEE"}
                            onChange={(e) => setRole(e.target.value)}
                        />
                        Tutee
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="role"
                            value="PARENT"
                            checked={role === "PARENT"}
                            onChange={(e) => setRole(e.target.value)}
                        />
                        Parent
                    </label>
                </div>
                <button type="submit" className="login-button">로그인</button>
            </form>
            {error && <p className="error">{error}</p>}
        </div>
    );
};

export default Login;
