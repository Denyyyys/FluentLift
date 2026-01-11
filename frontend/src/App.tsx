import { BrowserRouter, Route, Routes } from "react-router-dom"
import SignupPage from "./pages/auth/SignupPage"
import LoginPage from "./pages/auth/LoginPage"
import { RequireAuth } from "./components/auth/RequireAuth"
import ProfilePage from "./pages/profile/ProfilePage"
import MyDecksPage from "./pages/decks/MyDecksPage"
import { Navbar } from "./components/common/Navbar"
import DeckStudyPage from "./pages/decks/DeckStudyPage"
import MyCoursesPage from "./pages/courses/MyCoursesPage"
import CreateCoursePage from "./pages/courses/CreateCoursePage"
import AllCoursesPage from "./pages/courses/AllCoursesPage"
import CourseOverview from "./routes/CourseOverview"
import { ToastContainer } from 'react-toastify';
import CourseLayout from "./layouts/CourseLayout"
import RouteNotFound from "./pages/common/RouteNotFound"
import StudyLessonPage from "./pages/courses/StudyLessonPage"
import CreateDeckPage from "./pages/decks/CreateDeckPage"
import EditDeckPage from "./pages/decks/EditDeckPage"
import AllQuestionsPage from "./pages/questionsAndAnswers/AllQuestionsPage"
import { SingleQuestionPage } from "./pages/questionsAndAnswers/SingleQuestionPage"
import AllDecksPage from "./pages/decks/AllDecksPage"
import IndexPage from "./pages/IndexPage"

function App() {
  return <BrowserRouter>
    <ToastContainer position="top-center" />
    <Navbar />
    <div className="app-container">

      <Routes>
        <Route index element={<IndexPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route
          path="/profile/"
          element={
            <RequireAuth>
              <ProfilePage />
            </RequireAuth>
          }
        />

        <Route
          path="/decks/"
          element={
            <RequireAuth>
              <AllDecksPage />
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

        <Route
          path="/decks/new/"
          element={
            <RequireAuth>
              <CreateDeckPage />
            </RequireAuth>
          }
        />

        <Route path="/profile/decks/:deckId/study/" element={
          <RequireAuth>
            <DeckStudyPage />
          </RequireAuth>
        } />

        <Route path="/profile/decks/:deckId/settings/" element={
          <RequireAuth>
            <EditDeckPage />
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
          path="/profile/courses/new/"
          element={
            <RequireAuth>
              <CreateCoursePage />
            </RequireAuth>
          }
        />

        <Route path="/courses/"
          element={
            <RequireAuth>
              <AllCoursesPage />
            </RequireAuth>
          }
        />

        <Route path="/questions/"
          element={
            <RequireAuth>
              <AllQuestionsPage />
            </RequireAuth>
          }
        />

        <Route path="/questions/:questionId" element={
          <RequireAuth>
            <SingleQuestionPage />
          </RequireAuth>
        } />

        <Route path="/courses/:courseId"
          element={
            <RequireAuth>
              <CourseLayout />
            </RequireAuth>
          }
        >
          <Route index element={<CourseOverview />} />
          <Route path="lessons/:lessonId" element={<StudyLessonPage />} />
        </Route>



        <Route path="*" element={<RequireAuth><RouteNotFound /></RequireAuth>} />
      </Routes>
    </div>
  </BrowserRouter>


}

export default App
