import { useState, createContext, useEffect } from "react";
import Footer from "./Footer";
import Navigation from "./Navigation";
import { ILoginCredentials } from "../util/interface";
import decodeJwtToken from "../util/jwtToken";
import useGetUserByIdQuery from "../services/getUserById";

export const LoginContext = createContext<{
  loginCredentials: ILoginCredentials;
  setLoginCredentials: React.Dispatch<React.SetStateAction<ILoginCredentials>>;
}>({
  loginCredentials: {
    success: false,
    user: {
      id: "",
      fullName: "",
      email: "",
      address: "",
      birthday: new Date(),
      role: "",
    },
    jwtToken: "",
  },
  setLoginCredentials: () => {},
});

const Layout: React.FC<{ children: JSX.Element }> = ({ children }) => {
  const jwtToken = localStorage.getItem("jwt") || "";
  const { isLoading, data } = useGetUserByIdQuery(
    decodeJwtToken(jwtToken)?.sub || ""
  );
  const [loginCredentials, setLoginCredentials] = useState<ILoginCredentials>({
    success: jwtToken !== "" ? true : false,
    user: {
      id: "",
      fullName: "",
      email: "",
      address: "",
      birthday: new Date(),
      role: "",
    },
    jwtToken: jwtToken,
  });

  useEffect(() => {
    if (!isLoading && data) {
      setLoginCredentials((prevState) => ({
        ...prevState,
        user: {
          id: data.id,
          fullName: data.fullName,
          email: data.email,
          address: data.address,
          birthday: data.birthday,
          role: data.role,
        },
      }));
    }
  }, [isLoading, data]);

  return (
    <LoginContext.Provider value={{ loginCredentials, setLoginCredentials }}>
      <Navigation />
      {children}
      <Footer />
    </LoginContext.Provider>
  );
};

export default Layout;
