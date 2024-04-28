import React, { useContext, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { LoginContext } from "./Layout";
import LibraryIcon from "../assets/LibraryIcon";
import UserIcon from "../assets/UserIcon";
import { useOnClickOutside } from "usehooks-ts";
import ActionsIcon from "../assets/ActionsIcon";
import ListsIcon from "../assets/ListsIcon";

const Navigation: React.FC = () => {
  const { loginCredentials, setLoginCredentials } = useContext(LoginContext);
  const [showDropMenu, setShowDropMenu] = useState({
    user: false,
    actions: false,
    lists: false,
  });
  const userRef = useRef<HTMLDivElement | null>(null);
  const actionsRef = useRef<HTMLDivElement | null>(null);
  const listsRef = useRef<HTMLDivElement | null>(null);

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

  useOnClickOutside(userRef, () =>
    setShowDropMenu({ ...showDropMenu, user: false })
  );
  useOnClickOutside(actionsRef, () =>
    setShowDropMenu({ ...showDropMenu, actions: false })
  );
  useOnClickOutside(listsRef, () =>
    setShowDropMenu({ ...showDropMenu, lists: false })
  );

  return (
    <nav className="flex justify-between items-center border-b-[1px] shadow-lg bg-white p-2 phone:p-1">
      <Link to="/home" className="flex items-center mx-4">
        <LibraryIcon />
        <p className="font-bold text-2xl mx-2 phone:text-lg phone:mt-1">
          EDUCA LIBRARY
        </p>
      </Link>
      <div className="flex justify-center items-center">
        {loginCredentials.user.role === "ADMIN" && (
          <>
            <div>
              <div
                className="flex flex-col justify-center items-center mx-5 cursor-pointer"
                onClick={() =>
                  setShowDropMenu(
                    showDropMenu.actions
                      ? { ...showDropMenu, actions: false }
                      : { ...showDropMenu, actions: true }
                  )
                }>
                <ActionsIcon />
                <p className="font-bold text-sm">Actions</p>
              </div>
              {showDropMenu.actions && (
                <div
                  ref={actionsRef}
                  className="absolute bg-white border border-gray-200 py-2 mt-1 w-48 shadow-2xl">
                  <Link
                    to="/actions/create-book"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, actions: false })
                    }>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Create book
                    </button>
                  </Link>
                  <Link
                    to="/actions/create-author"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, actions: false })
                    }>
                    <button className="block w-full text-left px-4 py-3 text-sm text-gray-700 hover:bg-gray-100 font-semibold">
                      Create author
                    </button>
                  </Link>
                </div>
              )}
            </div>
            <div>
              <div
                className="flex flex-col justify-center items-center mx-5 cursor-pointer"
                onClick={() =>
                  setShowDropMenu(
                    showDropMenu.lists
                      ? { ...showDropMenu, lists: false }
                      : { ...showDropMenu, lists: true }
                  )
                }>
                <ListsIcon className="mt-[-4px]" />
                <p className="font-bold text-sm mt-[-4px]">Lists</p>
              </div>
              {showDropMenu.lists && (
                <div
                  ref={listsRef}
                  className="absolute bg-white border border-gray-200 py-2 mt-1 w-48 shadow-2xl">
                  <Link
                    to="/lists/users"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, lists: false })
                    }>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Users
                    </button>
                  </Link>
                  <Link
                    to="/lists/authors"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, lists: false })
                    }>
                    <button className="block w-full text-left px-4 py-3 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Authors
                    </button>
                  </Link>
                  <Link
                    to="/actions/rent-entries"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, lists: false })
                    }>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold">
                      Rent entries
                    </button>
                  </Link>
                </div>
              )}
            </div>
          </>
        )}

        <div>
          <div
            className="flex flex-col justify-center items-center mx-5 cursor-pointer"
            onClick={() =>
              setShowDropMenu(
                showDropMenu.user
                  ? { ...showDropMenu, user: false }
                  : { ...showDropMenu, user: true }
              )
            }>
            <UserIcon />
            {!loginCredentials.success ? (
              <p className="font-bold text-sm">Guest</p>
            ) : (
              <p className="font-bold text-sm">
                {loginCredentials.user.fullName}
              </p>
            )}
          </div>
          {showDropMenu.user && (
            <div
              ref={userRef}
              className="absolute right-0 bg-white border border-gray-200 py-2 mt-1 w-48 shadow-2xl">
              {loginCredentials.jwtToken ? (
                <>
                  <Link
                    to="/profile"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, user: false })
                    }>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Profile
                    </button>
                  </Link>
                  <button
                    onClick={() => {
                      handleLogout();
                      setShowDropMenu({ ...showDropMenu, user: false });
                    }}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold">
                    Logout
                  </button>
                </>
              ) : (
                <>
                  <Link
                    to="/login"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, user: false })
                    }>
                    <button className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 font-semibold border-b-[2px]">
                      Login
                    </button>
                  </Link>
                  <Link
                    to="/registration"
                    onClick={() =>
                      setShowDropMenu({ ...showDropMenu, user: false })
                    }>
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
