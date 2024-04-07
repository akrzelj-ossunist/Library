import React, { useContext, useEffect } from "react";
import { useLocation, useNavigate, useRoutes } from "react-router-dom";
import Login from "./pages/Login";
import Registration from "./pages/Registration";
import Home from "./pages/Home";
import { LoginContext } from "./components/Layout";
import Profile from "./pages/Profile";

const App: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();
  const location = useLocation();
  const { pathname } = location;
  const routes = useRoutes([
    {
      path: "/home",
      element: <Home />,
    },
    {
      path: "/login",
      element: <Login />,
    },
    {
      path: "/registration",
      element: <Registration />,
    },
    {
      path: "/profile",
      element: <Profile />,
    },
  ]);

  useEffect(() => {
    if (pathname === "/") {
      loginCredentials.success ? navigate("/home") : navigate("/login");
    }
  }, []);

  return <div>{routes}</div>;
};

export default App;
