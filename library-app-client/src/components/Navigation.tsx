import React, { useContext, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { LoginContext } from "./Layout";
import LibraryIcon from "../assets/LibraryIcon";
import UserIcon from "../assets/UserIcon";
import { useOnClickOutside } from "usehooks-ts";

const Navigation: React.FC = () => {
  const { loginCredentials, setLoginCredentials } = useContext(LoginContext);
  const [showDropMenu, setShowDropMenu] = useState(false);
  const dropDownRef = useRef<HTMLDivElement | null>(null);

  const handleLogout = () => {
    setLoginCredentials({
      user: {
        id: "",
        fullName: "",
        email: "",
        address: "",
        birthday: new Date(),
        role: "",
      },
      jwtToken: "",
      success: false,
    });
    localStorage.removeItem("jwt");
  };

  useOnClickOutside(dropDownRef, () => setShowDropMenu(false));
  console.log(loginCredentials);
  return (
    <nav className="flex justify-between items-center border-b-[1px] shadow-lg bg-white p-2">
      <Link to="/home" className="flex items-center mx-4">
        <LibraryIcon />
        <p className="font-bold text-2xl mx-2">EDUCA LIBRARY</p>
      </Link>
      <div className="flex justify-center items-center">
        <div>
          <div
            className="flex flex-col justify-center items-center mx-5 cursor-pointer"
            onClick={() => setShowDropMenu(showDropMenu ? false : true)}>
            <UserIcon className="text-blue-800 mt-1" />
            {!loginCredentials.success ? (
              <p className="font-bold text-sm text-blue-800">Guest</p>
            ) : (
              <p className="font-bold text-sm text-blue-800">
                {loginCredentials.user.fullName}
              </p>
            )}
          </div>
          {showDropMenu && (
            <div
              ref={dropDownRef}
              className="absolute right-0 bg-white border border-gray-200 py-2 mt-1 w-48 shadow-2xl">
              {loginCredentials.jwtToken ? (
                <>
                  <Link to="/profile" onClick={() => setShowDropMenu(false)}>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Profile
                    </button>
                  </Link>
                  <button
                    onClick={() => {
                      handleLogout();
                      setShowDropMenu(false);
                    }}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold">
                    Logout
                  </button>
                </>
              ) : (
                <>
                  <Link to="/login" onClick={() => setShowDropMenu(false)}>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Login
                    </button>
                  </Link>
                  <Link
                    to="/registration"
                    onClick={() => setShowDropMenu(false)}>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold">
                      Register
                    </button>
                  </Link>
                </>
              )}
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navigation;
