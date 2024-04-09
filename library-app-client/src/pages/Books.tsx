import BookList from "../components/BookList";
import FilterBooks from "../components/FilterBooks";

const Books: React.FC = () => {
  return (
    <div className="flex justify-center items-center flex-1 flex-grow flex-col">
      <FilterBooks />
      <BookList />
    </div>
  );
};

export default Books;
