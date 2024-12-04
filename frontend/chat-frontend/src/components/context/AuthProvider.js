import React, { createContext, useContext, useEffect, useState } from 'react';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    console.log('AuthProvider Rendered')

    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null); // 사용자 정보 저장
    const [role, setRole] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        console.log('AuthProvider useEffect 호출')
        const token = localStorage.getItem('jwtToken');

        if (token) {
            const decodedUser = parseJwt(token);
            console.log('decodedUser: ', decodedUser)

            // 토큰 유효성 검증
            if (decodedUser && isTokenValid(token)) {
                setIsLoggedIn(true);
                setUser(decodedUser);
            } else {
                localStorage.removeItem('jwtToken'); // 유효하지 않은 토큰 제거
            }
        }
        setIsLoading(false);
    }, []);

    const login = (userData, token, role) => {
        localStorage.setItem('jwtToken', token);
        setIsLoggedIn(true);
        setUser(userData);
        setRole(role);
    };

    const logout = () => {
        localStorage.removeItem('jwtToken');
        setIsLoggedIn(false);
        setRole(null);
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, user, role, login, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    );    
};

export default AuthProvider;

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

const parseJwt = (token) => {
    try {
        const base64Payload = token.split('.')[1];
        const payload = atob(base64Payload);
        return JSON.parse(payload);
    } catch (e) {
        console.error('Failed to parse JWT:', e);
        return null;
    }
};

const isTokenValid = (token) => {
    try {
        const decoded = parseJwt(token);
        if (!decoded) return false;

        const now = Math.floor(Date.now() / 1000); // 현재 시간 (초 단위)
        return decoded.exp > now; // 토큰 만료 시간 확인
    } catch (e) {
        console.error('Invalid token:', e);
        return false;
    }
};