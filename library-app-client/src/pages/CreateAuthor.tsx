import { useContext, useEffect } from "react";
import { LoginContext } from "../components/Layout";
import { useNavigate } from "react-router-dom";
import * as yup from "yup";
import { Field, Form, Formik } from "formik";
import axios from "axios";
import decodeJwtToken from "../util/jwtToken";

const CreateAuthor: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const navigate = useNavigate();
  useEffect(() => {
    !loginCredentials.success && navigate("/login");
    decodeJwtToken(loginCredentials.jwtToken)?.scope !== "ADMIN" &&
      navigate("/home");
  }, [loginCredentials.success]);

  const author = {
    fullName: "",
    birthday: new Date(),
  };

  const authorSchema = yup.object().shape({
    fullName: yup
      .string()
      .min(2, "Username to short!")
      .max(30, "Username to long!")
      .required("You need to enter username!"),
    birthday: yup.date().required("You need to fill this box"),
  });

  const handleSubmit = async (values: any) => {
    const headers = { Authorization: `Bearer ${loginCredentials.jwtToken}` };
    try {
      const response = await axios.post(
        "http://localhost:8081/api/v1/author/create",
        values,
        { headers }
      );
      console.log(await response.data);
      window.location.reload();
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      <div className="flex absolute -z-10 top-0 h-[100vh] overflow-hidden">
        <div className="w-2/5 tablet:hidden">
          <img
            src="https://images.squarespace-cdn.com/content/v1/604ffb544c6d436ffc845808/1618518728259-7VWFGFY3W6L62B1ZTBSN/author+facts.jpg"
            alt="libraryImg"
            className="w-full object-cover object-center h-full mr-[5%]"
          />
        </div>
        <div className="w-3/5 tablet:m-0 tablet:w-full flex flex-col mx-[7%] justify-center items-center">
          <p className="m-5 font-bold text-3xl">Create Author</p>
          <Formik
            initialValues={author}
            validationSchema={authorSchema}
            onSubmit={(values, actions) => {
              actions.setSubmitting(false);
              handleSubmit(values);
            }}>
            {({ errors, touched }) => {
              return (
                <Form className="my-10 w-[400px] phone:w-[300px] m-5">
                  <div className="mb-10 flex flex-col">
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
                      Create
                    </button>
                  </div>
                </Form>
              );
            }}
          </Formik>
        </div>
      </div>
    </>
  );
};

export default CreateAuthor;
