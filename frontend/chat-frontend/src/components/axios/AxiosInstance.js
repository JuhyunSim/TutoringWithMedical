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
        const portOneSecret = process.env.REACT_APP_PORTONE_SECRET; // .env 파일에서 API Secret 가져오기

        // 요청 URL이 PortOne API인지 확인
        const isPortOneApi = config.baseURL?.includes("api.portone.io");

        if (isPortOneApi) {
            // PortOne API 요청에 PortOne Secret 추가
            if (portOneSecret) {
                config.headers['Authorization'] = `PortOne ${portOneSecret}`;
            }
        } else if (token) {
            // 다른 요청은 JWT 토큰 사용
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default axiosInstance;
