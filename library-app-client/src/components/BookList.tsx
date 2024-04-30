import queryString from "query-string";
import { useLocation, useNavigate } from "react-router-dom";
import { IBook, IBookFilters } from "../util/interface";
import useGetFilteredBooksQuery from "../services/getFilteredBooks";
import TableComponent from "./TableComponent";
import { createColumnHelper } from "@tanstack/react-table";
import { concatArrayOfArrays } from "../util/concatArrayOfArrays";
import { cleanEmptyUrlParams } from "../util/cleanEmptyUrlParams";
import { useEffect, useState } from "react";
import RentBookModalContent from "./RentBookModalContent";

const BookList: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { genre, available, book, author, isbn } = queryString.parse(
    location.search
  );

  const [showModal, setShowModal] = useState(false);
  const [bookId, setBookId] = useState("");
  const [showIfRented, setShowIfRented] = useState({
    successfully: false,
    unsuccessfully: false,
  });

  const filterParams: IBookFilters = {
    genre: genre || "",
    available: available !== undefined ? available === "true" : null,
    book: book || "",
    author: author || "",
    isbn: isbn || "",
  };

  const { isLoading, data: bookData } = useGetFilteredBooksQuery(filterParams);

  useEffect(() => {
    navigate(`/books?${cleanEmptyUrlParams()}`);
  }, [window.location.search]);

  const columnHelper = createColumnHelper<IBook>();

  const columns = [
    // columnHelper.accessor("id", {
    //   header: () => "ID",
    //   cell: (info) => info.getValue(),
    // }),
    columnHelper.accessor("isbn", {
      header: () => "Isbn",
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("title", {
      header: () => "Title",
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("author.fullName", {
      header: () => "Author",
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("isAvailable", {
      header: () => "Available",
      cell: (info) => info.getValue(),
    }),
    columnHelper.accessor("genre", {
      header: () => "Genre",
      cell: (info) => info.getValue(),
    }),
  ];

  return (
    <div className="flex w-full justify-center">
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <>
          <div
            className={`fixed top-20 right-5 w-[300px] h-10 rounded-lg flex justify-center items-center p-2 bg-green-500 text-white font-bold ease-in-out duration-1000 -z-10 ${
              showIfRented.successfully ? "opacity-100" : "opacity-0"
            }`}>
            <p>Book rented successfully</p>
          </div>

          <div
            className={`fixed top-20 right-5 w-[300px] h-10 rounded-lg flex justify-center items-center p-2 bg-red-500 text-white font-bold ease-in-out duration-1000 -z-10 ${
              showIfRented.unsuccessfully ? "opacity-100" : "opacity-0"
            }`}>
            <p>Book renting failed</p>
          </div>
          {showModal && (
            <RentBookModalContent
              setShowModal={setShowModal}
              bookId={bookId}
              setShowIfRented={setShowIfRented}
            />
          )}
          <TableComponent
            data={concatArrayOfArrays(bookData?.pages || []) ?? []}
            columns={columns}
            setBookId={setBookId}
            setShowModal={setShowModal}
          />
        </>
      )}
    </div>
  );
};

export default BookList;
