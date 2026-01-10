import { createBrowserRouter, RouterProvider, Navigate } from "react-router-dom";
import FrontPage from "./Pages/FrontPage";
import QuizOrMalPage from "./Pages/QuizOrMalPage";
import QuizPaths from "./Pages/Paths";

const router = createBrowserRouter([
  { path: "/", element: <FrontPage /> },
  { path: "/quiz-or-mal", element: <QuizOrMalPage /> },
  { path: "*", element: <Navigate to="/" replace /> },
  { path: "/quiz-paths", element: <QuizPaths /> }
]);

export function AppRoutes() {
  return <RouterProvider router={router} />;
}
