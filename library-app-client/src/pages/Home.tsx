import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { LoginContext } from "../components/Layout";
import FilterBooks from "../components/FilterBooks";

const Home: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();

  useEffect(() => {
    !loginCredentials.success && navigate("/login");
  }, [loginCredentials.success]);

  return (
    <div className="flex justify-center items-center flex-1 flex-grow">
      <FilterBooks />
    </div>
  );
};

export default Home;
