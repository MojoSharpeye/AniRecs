import { createBrowserRouter, RouterProvider } from "react-router-dom";
import FrontPage from "./Pages/FrontPage";

const router = createBrowserRouter([
  { path: "/", element: <FrontPage /> },
]);

export function AppRoutes() {
  return <RouterProvider router={router} />;
}
