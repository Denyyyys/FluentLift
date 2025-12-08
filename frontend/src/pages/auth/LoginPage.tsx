import { Link, useLocation, useNavigate } from "react-router-dom"
import { useAuth } from "../../context/AuthContext"
import { useState } from "react";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import type { LoginInfo } from "../../types/user";
import { toast } from "react-toastify";

function LoginPage() {
    const { login, isLoggedIn } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from?.pathname || "/";

    const [loginInfo, setLoginInfo] = useState({ email: "", password: "" } satisfies LoginInfo);
    if (isLoggedIn) {
        toast.success("Successfully logged in!")
        navigate(from)
    }
    return (
        <form className="mt-4 authentication-form p-5 rounded" onSubmit={async (e) => {
            e.preventDefault();
            if (!loginInfo.email || !loginInfo.password) {
                toast.error("Email and Password cannot be empty")
                return
            }
            try {
                const response = await axios.post<string>(`${BACKEND_BASE_URL}/auth/generateToken`, loginInfo)
                login(response.data);
                navigate("/")
            } catch (error) {
                console.log(error);
                if (axios.isAxiosError(error)) {
                    if (error.status === 401 && error.response?.data === "Authentication failed: Bad credentials") {
                        toast.error("No account found with that email, or the password is incorrect.")
                    }
                } else {
                    toast.error("Unexpected error happened")
                }
            }
        }}>
            <h2 className="mb-3 text-center">Log In to Continue</h2>

            <div className="mb-3">
                <label htmlFor="inputEmail" className="form-label"><h4>Email address</h4></label>
                <input type="email" className="form-control" value={loginInfo.email} onChange={(e) => setLoginInfo(prev => ({ ...prev, email: e.target.value }))} id="inputEmail" />
            </div>
            <div className="mb-3">
                <label htmlFor="inputPassword" className="form-label"><h4>Password</h4></label>
                <input type="password" className="form-control" id="inputPassword" value={loginInfo.password} onChange={(e) => setLoginInfo(prev => ({ ...prev, password: e.target.value }))} />
            </div>
            <button type="submit" className="btn btn-primary container-fluid">Submit</button>
            <h5 className="text-center">Don't have an account? <Link to="/signup">Sign Up</Link>  </h5>

        </form>
    )
}

export default LoginPage