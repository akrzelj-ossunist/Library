import queryString from "query-string";
import { useLocation } from "react-router-dom";
import { IBookFilters } from "../util/interface";

const BookList: React.FC = () => {
  const location = useLocation();
  const { genre, available, search } = queryString.parse(location.search);
  const filterParams: IBookFilters = {
    genre: genre,
    available: available !== null ? true : false,
    search: search,
  };
  //const { isLoading, data: books } = useGetFilteredBooksQuery(filterParams);

  return (
    <div>
      <h2>Genre: {filterParams.genre}</h2>
      <h2>Available: {filterParams.available?.toString()}</h2>
      <h2>Search: {filterParams.search}</h2>
    </div>
  );
};

export default BookList;
