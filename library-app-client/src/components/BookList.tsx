import queryString from "query-string";
import { useLocation, useNavigate } from "react-router-dom";
import { IBook, IBookFilters } from "../util/interface";
import useGetFilteredBooksQuery, {
  useGetFilteredBooksQueryMock,
} from "../services/getFilteredBooks";
import TableComponent from "./TableComponent";
import { createColumnHelper } from "@tanstack/react-table";
import { concatArrayOfArrays } from "../util/concatArrayOfArrays";
import { cleanEmptyUrlParams } from "../util/cleanEmptyUrlParams";
import { useEffect } from "react";

const BookList: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { genre, available, book, author, isbn } = queryString.parse(
    location.search
  );

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
        <TableComponent
          data={concatArrayOfArrays(bookData?.pages || []) ?? []}
          columns={columns}
        />
      )}
    </div>
  );
};

export default BookList;
