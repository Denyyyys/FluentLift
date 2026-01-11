import { Link, useLocation, useNavigate } from "react-router-dom"
import { useAuth } from "../../context/AuthContext";
import type { SignUpInfo } from "../../types/user";
import { useState } from "react";
import { toast } from "react-toastify";
import axios from "axios";
import { BACKEND_BASE_URL } from "../../constants";
import { useLanguage } from "../../hooks/useLanguage";
import { textByLanguage } from "../../assets/translations";

function SignupPage() {
    const { login, isLoggedIn } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const { language } = useLanguage();
    const from = location.state?.from?.pathname || "/";
    const [signUpInfo, setSignUpInfo] = useState({ email: "", password: "", name: "" } satisfies SignUpInfo);

    if (isLoggedIn) {
        toast.success("Successfully logged in!")
        navigate(from)
    }

    return (
        <form className="mt-4 authentication-form p-5 rounded shadow" onSubmit={async (e) => {
            e.preventDefault();
            if (!signUpInfo.email || !signUpInfo.password || !signUpInfo.name) {
                toast.error("Please fill all fields")
                return
            }
            try {
                const response = await axios.post<string>(`${BACKEND_BASE_URL}/auth/addNewUser`, signUpInfo)
                login(response.data);
                navigate("/")
            } catch (error) {
                console.log(error);
                if (axios.isAxiosError(error)) {
                    if (error.status === 400 && error.response?.data === "User already exists: User with provided credentials already exists") {
                        toast.error("Probably user with provided credentials already exist. Try to Log in")
                    }
                } else {
                    toast.error("Unexpected error happened")
                }
            }
        }}
        >
            <h2 className="mb-3 text-center">{textByLanguage[language]["authenticatoin"]["createNewAccountText"]}</h2>
            <div className="mb-3">
                <label htmlFor="inputName" className="form-label"><h4>{textByLanguage[language]["authenticatoin"]["nameText"]}</h4></label>
                <input type="text" className="form-control" id="inputName" value={signUpInfo.name} onChange={e => setSignUpInfo(prev => ({ ...prev, name: e.target.value }))} />
            </div>
            <div className="mb-3">
                <label htmlFor="inputEmail" className="form-label"><h4>{textByLanguage[language]["authenticatoin"]["emailAddressText"]}</h4></label>
                <input type="email" className="form-control" id="inputEmail" value={signUpInfo.email} onChange={e => setSignUpInfo(prev => ({ ...prev, email: e.target.value }))} />
            </div>
            <div className="mb-3">
                <label htmlFor="inputPassword" className="form-label"><h4>{textByLanguage[language]["authenticatoin"]["passwordText"]}</h4></label>
                <input type="password" className="form-control" id="inputPassword" value={signUpInfo.password} onChange={e => setSignUpInfo(prev => ({ ...prev, password: e.target.value }))} />
            </div>
            <button type="submit" className="btn btn-primary container-fluid mb-3">{textByLanguage[language]["authenticatoin"]["signUpText"]}</button>
            <h5 className="text-center">{textByLanguage[language]["authenticatoin"]["alreadyHaveAccountQuestionText"]} <Link to="/login">{textByLanguage[language]["authenticatoin"]["logInText"]}</Link>  </h5>
        </form>
    )
}

export default SignupPage