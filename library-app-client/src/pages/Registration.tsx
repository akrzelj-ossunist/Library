import { IRegisterForm } from "../util/interface";
import { userRegister } from "../services/userRegister";
import { Link, useNavigate } from "react-router-dom";
import * as yup from "yup";
import { Form, Field, Formik } from "formik";
import { useContext, useEffect } from "react";
import { LoginContext } from "../components/Layout";

const Registration: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();

  const register: IRegisterForm = {
    fullName: "",
    email: "",
    emailRepeat: "",
    password: "",
    passwordRepeat: "",
    address: "",
    birthday: new Date(),
  };

  useEffect(() => {
    if (loginCredentials.jwtToken !== "") {
      navigate("/home");
    }
  }, [loginCredentials.success]);

  const registerSchema = yup.object().shape({
    fullName: yup
      .string()
      .min(2, "Username to short!")
      .max(20, "Username to long!")
      .required("You need to enter username!"),
    password: yup
      .string()
      .min(8, "Password must contain minimum of 8 characters")
      .required("You need to enter password!"),
    passwordRepeat: yup
      .string()
      .oneOf([yup.ref("password")], "Password must match"),
    email: yup
      .string()
      .email("Invalid email")
      .required("You must input email!"),
    emailRepeat: yup.string().oneOf([yup.ref("email")], "Email must match!"),
    address: yup
      .string()
      .min(5, "Address to short!")
      .max(30, "Address to long!")
      .required("You need to enter address!"),
    birthday: yup.date().required("You need to fill this box"),
  });

  const handleSubmit = async (values: IRegisterForm) => {
    const response = userRegister(values);
    console.log(await response);
    navigate("/login");
  };

  return (
    <div className="flex w-full absolute -z-10 top-0 h-[100vh] overflow-hidden">
      <div className="w-2/5 tablet:hidden">
        <img
          src="https://media.npr.org/assets/img/2023/12/29/gettyimages-925364372-edit_custom-e8624c50d288b68773b1918dd675fa4c1d787e79.jpg"
          alt="libraryImg"
          className="w-full object-cover object-center h-full mr-[5%]"
        />
      </div>
      <div className="w-3/5 tablet:m-0 tablet:w-full flex flex-col mt-28 mx-[7%] overflow-y-auto overflow-x-hidden">
        <p className="mx-5 tablet:mt-28 font-bold text-3xl">
          Registration Page
        </p>
        <Formik
          initialValues={register}
          validationSchema={registerSchema}
          onSubmit={(values, actions) => {
            actions.setSubmitting(false);
            handleSubmit(values);
          }}>
          {({ errors, touched }) => {
            return (
              <Form className="my-5 w-[520px] tablet:w-full m-5">
                <div className="mb-10 flex flex-col tablet:mr-10">
                  <label>Full name:</label>
                  <Field
                    className="border-b-2 border-black p-2 text-lg focus:outline-none"
                    type="text"
                    name="fullName"
                  />
                  {errors.fullName && touched.fullName && (
                    <label className="text-sm text-red-500 font-bold">
                      {errors.fullName}
                    </label>
                  )}
                </div>
                <div className="flex tablet:flex-col w-full justify-between">
                  <div className="flex flex-col mb-10 desktop:w-[47%] tablet:mr-10">
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
                  <div className="flex flex-col mb-10 tablet:mr-10 desktop:w-[47%]">
                    <label>Repeat password: </label>
                    <Field
                      className="border-b-2 border-black p-2 text-lg focus:outline-none"
                      type="password"
                      name="passwordRepeat"
                    />
                    {errors.passwordRepeat && touched.passwordRepeat && (
                      <label className="text-sm text-red-500 font-bold">
                        {errors.passwordRepeat}
                      </label>
                    )}
                  </div>
                </div>
                <div className="flex tablet:flex-col justify-between tablet:mr-10">
                  <div className="flex flex-col mb-10 desktop:w-[47%] desktop:mr-10">
                    <label>Email: </label>
                    <Field
                      className="border-b-2 border-black p-2 text-lg focus:outline-none"
                      type="email"
                      name="email"
                    />
                    {errors.email && touched.email && (
                      <label className="text-sm text-red-500 font-bold">
                        {errors.email}
                      </label>
                    )}
                  </div>
                  <div className="flex flex-col desktop:w-[47%] mb-10">
                    <label>Repeat email: </label>
                    <Field
                      className="border-b-2 border-black p-2 text-lg focus:outline-none"
                      type="email"
                      name="emailRepeat"
                    />
                    {errors.emailRepeat && touched.emailRepeat && (
                      <label className="text-sm text-red-500 font-bold">
                        {errors.emailRepeat}
                      </label>
                    )}
                  </div>
                </div>
                <div className="flex-col mb-10 flex tablet:mr-10">
                  <label>Address:</label>
                  <Field
                    className="border-b-2 border-black p-2 text-lg focus:outline-none"
                    type="text"
                    name="address"
                  />
                  {errors.address && touched.address && (
                    <label className="text-sm text-red-500 font-bold">
                      {errors.address}
                    </label>
                  )}
                </div>
                <div className="flex flex-col mb-10 w-[50%]">
                  <label>Birthday: </label>
                  <Field
                    className="border-b-2 border-black p-2 text-lg focus:outline-none"
                    type="date"
                    name="birthday"
                  />
                  {errors.birthday && touched.birthday && (
                    <label className="text-sm text-red-500 font-bold">
                      You need to fill this box!
                    </label>
                  )}
                </div>
                <div className="flex flex-col">
                  <button
                    type="submit"
                    className="text-white cursor-pointer font-bold w-[200px] rounded-xl text-2xl bg-blue-500 py-3 active:bg-blue-300 shadow-lg">
                    Register
                  </button>
                  <div className="flex items-center mt-2">
                    <p>Already have account... </p>
                    <Link
                      to="/login"
                      className="text-sm font-bold text-blue-500 underline cursor-pointer ml-1">
                      Login here
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

export default Registration;
