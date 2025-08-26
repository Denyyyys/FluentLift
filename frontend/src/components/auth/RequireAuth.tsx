import type { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

interface RequireAuthProps {
    children: ReactNode;
    redirectTo?: string;
}

export function RequireAuth({ children, redirectTo = "/login" }: RequireAuthProps) {
    const { token, loading } = useAuth();

    if (loading) return <div>Loading...</div>;
    if (!token) {
        return <Navigate to={redirectTo} replace />;
    }

    return children;
}