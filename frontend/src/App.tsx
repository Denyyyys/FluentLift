import { BrowserRouter, Route, Routes } from "react-router-dom"
import SignupPage from "./pages/auth/SignupPage"
import LoginPage from "./pages/auth/LoginPage"
import { RequireAuth } from "./components/auth/RequireAuth"
import ProfilePage from "./pages/profile/ProfilePage"
import MyDecksPage from "./pages/decks/MyDecksPage"
import { Navbar } from "./components/common/Navbar"
import DeckStudyPage2 from "./pages/decks/DeckStudyPage"
import MyCoursesPage from "./pages/courses/MyCoursesPage"
import CreateCoursePage from "./pages/courses/CreateCoursePage"

function App() {
  return <BrowserRouter>
    <Navbar />
    <div className="app-container">

      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route
          path="/profile"
          element={
            <RequireAuth>
              <ProfilePage />
            </RequireAuth>
          }
        />
        <Route
          path="/profile/decks/"
          element={
            <RequireAuth>
              <MyDecksPage />
            </RequireAuth>
          }
        />

        <Route path="/profile/decks/:deckId/study" element={
          <RequireAuth>
            <DeckStudyPage2 />
          </RequireAuth>
        } />

        <Route
          path="/profile/courses/"
          element={
            <RequireAuth>
              <MyCoursesPage />
            </RequireAuth>
          }
        />

        <Route
          path="/profile/courses/new"
          element={
            <RequireAuth>
              <CreateCoursePage />
            </RequireAuth>
          }
        />
      </Routes>
    </div>
  </BrowserRouter>
}

export default App
