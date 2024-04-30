import { useContext, useEffect } from "react";
import BookList from "../components/BookList";
import FilterBooks from "../components/FilterBooks";
import { LoginContext } from "../components/Layout";
import { useNavigate } from "react-router-dom";

const Books: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();
  useEffect(() => {
    !loginCredentials.success && navigate("/login");
  }, [loginCredentials.success]);
  return (
    <div className="flex justify-center items-center flex-1 flex-grow flex-col">
      <FilterBooks />
      <BookList />
    </div>
  );
};

export default Books;
