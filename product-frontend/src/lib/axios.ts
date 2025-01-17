import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL;    // 'http://localhost:8086/api/v1'

export const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
    'Content-Type': 'application/json',
},
});

axiosInstance.interceptors.request.use(
    (config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
},
(error) => {
    return Promise.reject(error);
}
);

axiosInstance.interceptors.response.use(
(response) => response,
    async (error) => {
    if (error.response?.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
    }
    return Promise.reject(error);
}
);
