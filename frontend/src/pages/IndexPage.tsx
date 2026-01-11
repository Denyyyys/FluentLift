import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function IndexPage() {
    const navigate = useNavigate();

    const { token } = useAuth();
    const isLoggedIn = token !== null
    useEffect(() => {
        if (isLoggedIn) {
            navigate("/profile/decks");
        } else {
            navigate("/signup");
        }
    }, [isLoggedIn, navigate]);
    return <></>
}
