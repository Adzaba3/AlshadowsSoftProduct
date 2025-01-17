

import { axiosInstance } from '@/lib/axios';


interface User {
    id: number;
    username: string;
    roles: string[];
}

interface LoginResponse {
    token: string;
    user: User;
}

interface LoginCredentials {
    username: string;
    password: string;
}

interface ApiResponse<T> {
    status: string;
    errorCode: string;
    message: string;
    data: T;
    links: Record<string, string>;
}

export const authService = {
    login: async (credentials: LoginCredentials): Promise<LoginResponse> => {
        const response = await axiosInstance.post<ApiResponse<LoginResponse>>('/auth/login', credentials);
        const loginData = response.data.data; // Extraction des données de l'ApiResponse
        
        if (loginData.token) {
            localStorage.setItem('token', loginData.token);
            localStorage.setItem('user', JSON.stringify(loginData.user));
        }
        
        return loginData;
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user'); // Nettoyer aussi les infos utilisateur
        window.location.href = '/login';
    },

    isAuthenticated: (): boolean => {
        return !!localStorage.getItem('token');
    },

    // Méthode utilitaire pour récupérer l'utilisateur courant
    getCurrentUser: (): User | null => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    // Méthode utilitaire pour vérifier si l'utilisateur a un rôle spécifique
    hasRole: (role: string): boolean => {
        const user = authService.getCurrentUser();
        return user?.roles.includes(role) ?? false;
    }
};