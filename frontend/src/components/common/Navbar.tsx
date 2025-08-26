import { Link } from "react-router-dom";
import { FaCircleQuestion } from "react-icons/fa6";
import { FaUser } from "react-icons/fa";
import { SiBookstack } from "react-icons/si";
import { TbCardsFilled } from "react-icons/tb";

export function Navbar() {
    return (
        <div className="navbar-container fixed-top">
            <nav className="navbar ">
                <div className="navbar-left">
                    <Link to="/" className="navbar-brand">
                        FluentLift
                    </Link>
                </div>
                <div className="navbar-right">
                    <Link to="/questions" className="nav-link"><FaCircleQuestion /> Q&A</Link>
                    <div className="dropdown nav-link">
                        <button className="btn dropdown-toggle nav-link-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <SiBookstack /> Courses
                        </button>
                        <ul className="dropdown-menu">
                            <Link to="/profile/courses/" className="dropdown-item">My Courses</Link>
                            <Link to="/courses/" className="dropdown-item"> Browse</Link>
                        </ul>
                    </div>
                    <div className="dropdown nav-link">
                        <button className="btn dropdown-toggle nav-link-btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <TbCardsFilled /> Decks
                        </button>
                        <ul className="dropdown-menu">
                            <Link to="/profile/decks/" className="dropdown-item">My Decks</Link>
                            <Link to="/decks/" className="dropdown-item"> Browse</Link>
                        </ul>
                    </div>
                    <Link to="/profile" className="nav-link"><FaUser /> Profile</Link>
                </div>
            </nav>
        </div>
    );
}