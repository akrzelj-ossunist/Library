import { useContext, useEffect } from "react";
import { LoginContext } from "../components/Layout";
import { useNavigate } from "react-router-dom";

const Profile: React.FC = () => {
  const { loginCredentials } = useContext(LoginContext);
  const { user } = loginCredentials;
  const navigate = useNavigate();

  useEffect(() => {
    !loginCredentials.success && navigate("/login");
  }, []);

  return (
    <div className="container mx-auto">
      <h1 className="text-2xl font-bold mb-4">User Profile</h1>
      <div className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Full Name
          </label>
          <p className="text-gray-900">{user.fullName}</p>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Email
          </label>
          <p className="text-gray-900">{user.email}</p>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Address
          </label>
          <p className="text-gray-900">{user.address}</p>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Birthday
          </label>
          <p className="text-gray-900">{user.birthday.toString()}</p>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Role
          </label>
          <p className="text-gray-900">{user.role}</p>
        </div>
      </div>
    </div>
  );
};

export default Profile;
