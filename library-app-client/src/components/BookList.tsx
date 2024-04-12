import queryString from "query-string";
import { useLocation } from "react-router-dom";
import { IBookFilters } from "../util/interface";
import useGetFilteredBooksQuery from "../services/getFilteredBooks";

const BookList: React.FC = () => {
  const location = useLocation();
  const { genre, available, book, author, isbn } = queryString.parse(
    location.search
  );

  const filterParams: IBookFilters = {
    genre: genre,
    available: available !== null ? true : false,
    book: book,
    author: author,
    isbn: isbn,
  };
  const { isLoading, data: books } = useGetFilteredBooksQuery(filterParams);

  isLoading ? console.log("Loading...") : console.log(books);
  return <div className="flex w-full justify-center"></div>;
};

export default BookList;
