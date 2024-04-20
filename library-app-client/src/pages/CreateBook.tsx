import { useContext, useEffect } from "react";
import { LoginContext } from "../components/Layout";
import { useNavigate } from "react-router-dom";

const CreateBook: React.FC = () => {
  const { loginCredentials, setLoginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();
  useEffect(() => {
    loginCredentials.jwtToken === "" && navigate("/login");
    loginCredentials.user.role === "ADMIN" && navigate("/home");
  }, []);

  return <></>;
};

export default CreateBook;
