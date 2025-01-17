import { FC, ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { authService } from '@/services/auth/service';

interface ProtectedRouteProps {
    children: ReactNode;
}

export const ProtectedRoute: FC<ProtectedRouteProps> = ({ children }) => {
    const location = useLocation();

if (!authService.isAuthenticated()) {
    return <Navigate to="/login" state={{ from: location }} replace />;
}

return <>{children}</>;
};