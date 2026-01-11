import { Link } from "react-router-dom";
import { FaCircleQuestion } from "react-icons/fa6";
import { FaUser } from "react-icons/fa";
import { SiBookstack } from "react-icons/si";
import { TbCardsFilled } from "react-icons/tb";
import { useAuth } from "../../context/AuthContext";
import { Dropdown } from "react-bootstrap";
import { useLanguage } from "../../hooks/useLanguage";
import type { AVAILABLE_LANGUAGES_TYPE } from "../../constants";
import { textByLanguage } from "../../assets/translations";
export function Navbar() {
    const { userName, isLoggedIn } = useAuth()
    const { language, setLanguage } = useLanguage();
    const mapLanguageToIcon = (language: AVAILABLE_LANGUAGES_TYPE) => {
        switch (language) {
            case "English":
                return <span className="fi fi-gb"></span>
            case "Polish":
                return <span className="fi fi-pl"></span>
            case "Ukrainian":
                return <span className="fi fi-ua"></span>
            default:
                throw new Error("Seleced language is not supported now!");
        }
    }

    return (
        <div className="navbar-container">
            <nav className="navbar">
                <div className="navbar-left">
                    <Link to="/" className="navbar-brand">
                        FluentLift
                    </Link>
                </div>
                <div className="navbar-right">
                    {isLoggedIn &&
                        <>
                            <Link to="/questions" className="nav-link"><FaCircleQuestion />{textByLanguage[language]["navbar"]["q&aText"]}</Link>
                            <div className="dropdown nav-link">
                                <button className="btn dropdown-toggle nav-link-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <SiBookstack /> {textByLanguage[language]["navbar"]["coursesText"]}
                                </button>
                                <ul className="dropdown-menu">
                                    <Link to="/profile/courses/" className="dropdown-item">{textByLanguage[language]["navbar"]["myCoursesText"]}</Link>
                                    <Link to="/courses/" className="dropdown-item"> {textByLanguage[language]["navbar"]["browseText"]}</Link>
                                </ul>
                            </div>
                            <div className="dropdown nav-link">
                                <button className="btn dropdown-toggle nav-link-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <TbCardsFilled /> {textByLanguage[language]["navbar"]["decksText"]}
                                </button>
                                <ul className="dropdown-menu">
                                    <Link to="/profile/decks/" className="dropdown-item">{textByLanguage[language]["navbar"]["myDecksText"]}</Link>
                                    <Link to="/decks/" className="dropdown-item"> {textByLanguage[language]["navbar"]["browseText"]}</Link>
                                </ul>
                            </div>
                            <Dropdown className="nav-link">
                                <Dropdown.Toggle variant="link" className="lang-dropdown-toggle" id="dropdown-basic">
                                    {mapLanguageToIcon(language)}
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item onClick={() => setLanguage("English")}><span className="fi fi-gb" /> {textByLanguage[language]["navbar"]["englishText"]}</Dropdown.Item>
                                    <Dropdown.Item onClick={() => setLanguage("Ukrainian")}><span className="fi fi-ua" /> {textByLanguage[language]["navbar"]["ukrainianText"]}</Dropdown.Item>
                                    <Dropdown.Item onClick={() => setLanguage("Polish")}><span className="fi fi-pl" /> {textByLanguage[language]["navbar"]["polishText"]}</Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                            <Link to="/profile" className="nav-link"><FaUser /> {isLoggedIn ? `${textByLanguage[language]["navbar"]["helloText"]}, ${userName}` : 'Log in'} </Link>
                        </>
                    }
                </div>
            </nav>
        </div>
    );
}