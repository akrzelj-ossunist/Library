import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { LoginContext } from "../components/Layout";

const Home: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();

  useEffect(() => {
    !loginCredentials.success && navigate("/login");
  }, []);

  return (
    <>
      <p className="m-4 text-4xl font-bold">Home app</p>
      <p className="m-4 text-2xl font-bold">
        Welcome {loginCredentials.user.fullName}!
      </p>
    </>
  );
};

export default Home;
