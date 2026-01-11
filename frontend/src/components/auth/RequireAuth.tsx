import type { ReactNode } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import LoadingSpinner from "../common/LoadingSpinner";

interface RequireAuthProps {
    children: ReactNode;
    redirectTo?: string;
}

export function RequireAuth({ children, redirectTo = "/login" }: RequireAuthProps) {
    const { token, loading } = useAuth();
    const location = useLocation();
    if (loading) return <LoadingSpinner />;
    if (!token) {
        return <Navigate to={redirectTo} replace state={{ from: location }} />;
    }

    return children;
}