import queryString from "query-string";
import { useLocation } from "react-router-dom";
import { IBook, IBookFilters } from "../util/interface";
import useGetFilteredBooksQuery, {
  useGetFilteredBooksQueryMock,
} from "../services/getFilteredBooks";
import TableComponent from "./TableComponent";
import { createColumnHelper } from "@tanstack/react-table";

const BookList: React.FC = () => {
  const location = useLocation();
  const { genre, available, book, author, isbn } = queryString.parse(
    location.search
  );
  const filterParams: IBookFilters = {
    genre: genre,
    available: available === "null" ? null : available === "true",
    book: book,
    author: author,
    isbn: isbn,
  };
  const { isLoading, data: bookData } = useGetFilteredBooksQueryMock(
    filterParams,
    1
  );

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

  !isLoading && console.log(bookData?.data);
  return (
    <div className="flex w-full justify-center">
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <TableComponent data={bookData?.data ?? []} columns={columns} />
      )}
    </div>
  );
};

export default BookList;
