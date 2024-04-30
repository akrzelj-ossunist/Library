import { useContext, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { LoginContext } from "../components/Layout";
import decodeJwtToken from "../util/jwtToken";

const DeleteBook: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();
  useEffect(() => {
    !loginCredentials.success && navigate("/login");
    decodeJwtToken(loginCredentials.jwtToken)?.scope !== "ADMIN" &&
      navigate("/home");
  }, [loginCredentials.success]);

  const { bookId } = useParams();
  console.log(bookId);
  return <></>;
};

export default DeleteBook;
