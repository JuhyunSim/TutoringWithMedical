// axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: process.env.REACT_APP_BACKEND_URL,
    timeout: 5000, // 5초 타임아웃 설정
});

// 요청 인터셉터를 사용하여 모든 요청에 JWT 토큰을 추가
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;
