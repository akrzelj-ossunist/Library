import { useContext, useState } from "react";
import { useDebounceValue } from "usehooks-ts";
import ArrowDownIcon from "../assets/ArrowDownIcon";
import Modal from "./Modal";
import useGetUserByFullNameQuery from "../services/getUserByFullName";
import { LoginContext } from "./Layout";
import axios from "axios";

const RentBookModalContent: React.FC<{
  setShowModal: (val: boolean) => void;
  bookId: string;
  setShowIfRented: (val: any) => void;
}> = ({ setShowModal, bookId, setShowIfRented }) => {
  const { loginCredentials } = useContext(LoginContext);
  const [debouncedValue, setValue] = useDebounceValue("", 750);
  const [searchValue, setSearchValue] = useState("");
  const [showDropDown, setShowDropDown] = useState(true);
  const [userId, setUserId] = useState("");
  const { data } = useGetUserByFullNameQuery(debouncedValue || " ");

  const submitRentEntry = async (values: any) => {
    const headers = { Authorization: `Bearer ${loginCredentials.jwtToken}` };
    try {
      const response = await axios.post(
        "http://localhost:8081/api/v1/rent-entry/create",
        values,
        { headers }
      );
      const data = await response.data;
      setShowIfRented({ unsuccessfully: false, successfully: true });
    } catch (error) {
      setShowIfRented({ successfully: false, unsuccessfully: true });
      console.error(error);
    }
    setTimeout(() => {
      setShowIfRented({ unsuccessfully: false, successfully: false });
    }, 5000);
  };

  return (
    <>
      <Modal>
        <div className="flex flex-col mb-10 w-[500px] h-[300px] p-4 justify-center items-center rounded-lg bg-white relative">
          <div>
            <label className="font-semibold text-lg">User: </label>
            <div className="relative">
              <input
                type="text"
                value={searchValue}
                className="border-[1px] border-slate-200 w-full p-2 py-2 rounded-md text-lg"
                onChange={(event: any) => {
                  setSearchValue(event.target.value);
                  setValue(event.target.value);
                  setShowDropDown(true);
                }}
              />
              <ArrowDownIcon
                className="absolute right-3 top-4 text-slate-400 cursor-pointer"
                onClick={() => setShowDropDown(showDropDown ? false : true)}
              />
              {showDropDown && (
                <ul className="border-[1px] border-slate-200 w-full rounded-md bg-white absolute">
                  {data?.map((user: any, index: number) => (
                    <li
                      key={index}
                      onClick={() => {
                        setSearchValue(user.fullName);
                        setShowDropDown(false);
                        setUserId(user.id);
                      }}
                      className="cursor-pointer border-b-1 p-2 py-1">
                      {user.fullName}
                    </li>
                  ))}
                </ul>
              )}
            </div>
            <button
              className="text-white cursor-pointer font-bold w-[100%] rounded-xl text-2xl bg-blue-500 p-2 active:bg-blue-300 shadow-lg flex justify-center items-center mt-7"
              onClick={() => {
                submitRentEntry({ bookId: bookId, userId: userId });
                setShowModal(false);
              }}>
              Rent
            </button>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default RentBookModalContent;
