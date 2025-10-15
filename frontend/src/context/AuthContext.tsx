import { createContext, useContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import type { JwtPayload } from '../types/jwt';
import type { ReactNode } from 'react';

interface AuthContextType {
    token: string | null;
    email: string | null;
    userName: string | null;
    userId: number | null;
    isLoggedIn: boolean;
    loading: boolean;
    login: (token: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);


export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [token, setToken] = useState<string | null>(null);
    const [email, setEmail] = useState<string | null>(null);
    const [userName, setUserName] = useState<string | null>(null);
    const [userId, setUserId] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);
    console.log("console log from functiion: AuthContext.tsx - AuthProvider is called");
    const setUserFromToken = (token: string | null) => {
        if (!token) {
            setEmail(null);
            setUserName(null);
            setToken(null);
            setUserId(null);
            return;
        }
        try {
            const decoded = jwtDecode<JwtPayload>(token);
            setEmail(decoded.sub);
            setUserName(decoded.userName || null);
            setToken(token);
            setUserId(decoded.userId);
        } catch (e) {
            console.log("Error while decoding JWT:", e);
            setEmail(null);
            setUserName(null);
            setToken(null);
            setUserId(null);
        }
    };

    useEffect(() => {
        console.log("Use effect from: AuthContext.tsx - AuthProvider is called");
        const storedToken = localStorage.getItem("jwtToken");
        setUserFromToken(storedToken);
        setLoading(false);
    }, []);

    const login = (newToken: string) => {
        setLoading(true);
        localStorage.setItem("jwtToken", newToken);
        setUserFromToken(newToken);
        setLoading(false);
    };

    const logout = () => {
        localStorage.removeItem("jwtToken");
        setEmail(null);
        setUserName(null);
        setToken(null);
        setUserId(null);
    };

    const isLoggedIn = !!token;

    return (
        <AuthContext.Provider value={{ token, email, userName, isLoggedIn, loading, login, logout, userId }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used within AuthProvider");
    return context;
};
