import { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ILoginForm } from "../util/interface";
import { userLogin } from "../services/userLogin";
import { LoginContext } from "../components/Layout";
import * as yup from "yup";
import { Formik, Form, Field } from "formik";

const Login: React.FC = () => {
  const { loginCredentials, setLoginCredentials } = useContext(LoginContext);
  const [invalidLoginData, setInvalidLoginData] = useState(false);
  const navigate = useNavigate();
  const login: ILoginForm = {
    email: "",
    password: "",
  };

  const loginSchema = yup.object().shape({
    email: yup
      .string()
      .email("Invalid email")
      .required("You must input email!"),
    password: yup
      .string()
      .min(8, "Password must contain minimum of 8 characters")
      .required("You need to enter password!"),
  });

  useEffect(() => {
    if (loginCredentials.jwtToken !== "") {
      navigate("/home");
    }
  }, [loginCredentials.success]);

  const handleSubmit = async (loginForm: ILoginForm) => {
    const response = await userLogin(loginForm);
    const jwtToken = response.token;
    console.log(response);
    if (response.success) {
      setLoginCredentials({
        success: response.success,
        user: response.userResDto,
        jwtToken: jwtToken,
      });
      localStorage.setItem("jwt", jwtToken);
      navigate("/home");
    } else {
      setInvalidLoginData(true);
    }
  };

  return (
    <div className="flex absolute -z-10 top-0 h-[100vh] overflow-hidden">
      <div className="w-2/5 tablet:hidden">
        <img
          src="https://media.npr.org/assets/img/2023/12/29/gettyimages-925364372-edit_custom-e8624c50d288b68773b1918dd675fa4c1d787e79.jpg"
          alt="libraryImg"
          className="w-full object-cover object-center h-full mr-[5%]"
        />
      </div>
      <div className="w-3/5 tablet:m-0 tablet:w-full flex flex-col mx-[7%] justify-center items-center">
        <p className="m-5 font-bold text-3xl">Login Page</p>
        <Formik
          initialValues={login}
          validationSchema={loginSchema}
          onSubmit={(values, actions) => {
            actions.setSubmitting(false);
            handleSubmit(values);
          }}>
          {({ errors, touched }) => {
            return (
              <Form className="my-10 w-[400px] phone:w-[300px] m-5">
                <div className="mb-10 flex flex-col">
                  <label>Email:</label>
                  <Field
                    className="border-b-2 border-black p-2 text-lg focus:outline-none"
                    type="text"
                    name="email"
                  />
                  {errors.email && touched.email && (
                    <label className="text-sm text-red-500 font-bold">
                      {errors.email}
                    </label>
                  )}
                </div>
                <div className="flex flex-col mb-10">
                  <label>Password: </label>
                  <Field
                    className="border-b-2 border-black p-2 text-lg focus:outline-none"
                    type="password"
                    name="password"
                  />
                  {errors.password && touched.password && (
                    <label className="text-sm text-red-500 font-bold">
                      {errors.password}
                    </label>
                  )}
                </div>
                <div className="flex flex-col">
                  <button
                    type="submit"
                    className="text-white cursor-pointer font-bold w-[200px] rounded-xl text-2xl bg-blue-500 py-3 active:bg-blue-300 shadow-lg">
                    Login
                  </button>
                  {invalidLoginData && (
                    <label className="text-sm text-red-500 font-bold">
                      Invalid email or password!
                    </label>
                  )}
                  <div className="flex items-center mt-2">
                    <p>Still dont have account... </p>
                    <Link
                      to="/registration"
                      className="text-sm font-bold text-blue-500 underline cursor-pointer ml-1">
                      Register here
                    </Link>
                  </div>
                </div>
              </Form>
            );
          }}
        </Formik>
      </div>
    </div>
  );
};

export default Login;
