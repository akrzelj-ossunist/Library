import { useEffect, useState } from "react";
import LibraryIcon from "../assets/LibraryIcon";
import IconSearch from "../assets/IconSearch";
import Select from "react-select";
import { Link } from "react-router-dom";
import { IBookFilters } from "../util/interface";
import queryString from "query-string";

const FilterBooks: React.FC = () => {
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const { genre, available, book, author, isbn } = queryString.parse(
    location.search
  );
  const [filters, setFilters] = useState<IBookFilters>({
    book: book || "",
    isbn: isbn || "",
    author: author || "",
    genre: genre || "",
    available: (available === "null" ? null : available === "true") || null,
  });

  useEffect(() => {
    window.addEventListener("resize", () => setWindowWidth(window.innerWidth));
    return () => {
      window.removeEventListener("resize", () =>
        setWindowWidth(window.innerWidth)
      );
    };
  }, [windowWidth]);

  const genreOptions = [
    { value: "HORROR", label: "Horror" },
    { value: "THRILLER", label: "Thriller" },
    { value: "COMEDY", label: "Comedy" },
    { value: "ROMANCE", label: "Romance" },
    { value: "DRAMA", label: "Drama" },
  ];

  const isAvailableOptions = [
    { value: true, label: "Yes" },
    { value: false, label: "No" },
  ];
  return (
    <div className="w-full mt-[13%] phone:mt-0">
      <div className="flex items-center m-8 ml-[20%] tablet:ml-[10%] phone:ml-[5%]">
        <LibraryIcon className="h-[3em] w-[3em] tablet:h-[2em] tablet:w-[2em]" />
        <p className="font-bold text-4xl mx-2 tablet:text-2xl">EDUCA LIBRARY</p>
      </div>
      <div className="flex w-full justify-center">
        <input
          type="text"
          defaultValue={book ? book.toString() : ""}
          onChange={(elment) =>
            setFilters({ ...filters, book: elment?.target.value })
          }
          className="w-[50%] border-[1px] border-black rounded-full p-2 pl-6 mr-4 tablet:w-[70%] phone:w-[95%]"
          placeholder="Search for book title..."></input>
        {windowWidth < 850 ? (
          <Link
            to={`/books?genre=${filters.genre}&available=${
              filters.available
            }&book=${filters.book}&author=${filters.author}&isbn=${
              filters.isbn
            }&page=${1}`}
            className="w-[10%]">
            <button className="text-white cursor-pointer font-bold w-[100%] rounded-xl text-2xl bg-blue-500 p-2 active:bg-blue-300 shadow-lg flex justify-center items-center">
              <IconSearch />
            </button>
          </Link>
        ) : (
          <Link
            to={`/books?genre=${filters.genre}&available=${
              filters.available
            }&book=${filters.book}&author=${filters.author}&isbn=${
              filters.isbn
            }&page=${1}`}
            className="w-[10%]">
            <button className="text-white cursor-pointer font-bold w-[100%] rounded-xl text-2xl bg-blue-500 p-2 active:bg-blue-300 shadow-lg">
              Search
            </button>
          </Link>
        )}
      </div>
      <div className="flex items-center m-4 ml-[20%] tablet:ml-[10%] phone:ml-[5%] flex-wrap">
        <p className="font-semibold text-xl mx-2 tablet:text-lg">Filters: </p>
        <Select
          onChange={(elment) =>
            setFilters({ ...filters, genre: elment?.value || null })
          }
          options={genreOptions}
          defaultValue={
            genre
              ? {
                  value: genre,
                  label: genre
                    ? genre.toString().charAt(0).toUpperCase() +
                      genre.toString().slice(1).toLowerCase()
                    : null,
                }
              : null
          }
          className="w-[150px] m-2"
          placeholder="Genre..."
        />
        <Select
          onChange={(elment) =>
            setFilters({ ...filters, available: elment?.value! })
          }
          options={isAvailableOptions}
          className="w-[150px] m-2"
          defaultValue={
            available
              ? {
                  value: available === "true",
                  label: available
                    ? available === "true"
                      ? "Yes"
                      : "No"
                    : null,
                }
              : null
          }
          placeholder="Available..."
        />
        <input
          type="text"
          onChange={(elment) =>
            setFilters({ ...filters, author: elment?.target.value })
          }
          className="border-[1px] border-slate-300 m-2 p-[6px] rounded-md w-[150px]"
          defaultValue={author ? author.toString() : ""}
          placeholder="Author..."
        />
        <input
          type="text"
          onChange={(elment) =>
            setFilters({ ...filters, isbn: elment?.target.value })
          }
          className="border-[1px] border-slate-300 m-2 p-[6px] rounded-md w-[150px]"
          defaultValue={isbn ? isbn.toString() : ""}
          placeholder="ISBN..."
        />
      </div>
    </div>
  );
};

export default FilterBooks;
