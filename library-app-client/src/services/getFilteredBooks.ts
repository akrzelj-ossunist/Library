import axios from "axios";
import { IBook, IBookFilters } from "../util/interface";
import { useInfiniteQuery, useQuery, UseQueryResult } from "@tanstack/react-query";

const getFilteredBooks = async (filters: IBookFilters, page: number) => {
    const api = `http://localhost:8081/api/v1/book/query`;
    try {
        const response = await axios.get(api, {
            params: {
                page: page,
                size: 10,
                title: filters.book,
                isbn: filters.isbn,
                author: filters.author,
                genre: filters.genre,
                available: filters.available
            }
        });
        return response.data;
    } catch (error) {
        console.error(error);
    }
}

const useGetFilteredBooksQuery = (filters: IBookFilters) => {
    return useInfiniteQuery({
        queryKey: [filters],
        queryFn: ({ pageParam }) => getFilteredBooks(filters, pageParam),
        initialPageParam: 1,
        getNextPageParam: (lastPage, allPages, lastPageParam, allPageParams) =>
          lastPage.nextCursor,
      })
}

///////////////////////////////////////////////////////////////////////////////////////////////////
const getFilteredBooksMock = async (filters: IBookFilters, page: number) => {
    const linkMock = "/data/books_mock_data.json";
    try {
        const response = await axios.get(linkMock);
        let { page: pageNumber, size, totalPages, data } = await response.data;
        
        // Ensure that 'data' is an array before filtering
        if (!Array.isArray(data)) {
            throw new Error('Invalid data format: expected an array');
        }

        // Filter data based on the provided parameters
        data = data.filter((book: any) => {
            return (
                (filters.book !== "" ? filters.book?.toString().toLocaleLowerCase() === book.title.toLowerCase() : true) &&
                (filters.isbn !== "" ? filters.isbn?.toString().toLowerCase() === book.isbn.toLowerCase() : true) &&
                (filters.author !== "" ? filters.author?.toString().toLowerCase() === book.author.fullName.toLowerCase() : true) &&
                (filters.genre !== "" ? filters.genre?.toString().toLowerCase() === book.genre.toLowerCase() : true) &&
                (filters.available !== null ? filters.available === book.isAvailable : true)
            );
        });

        // Calculate pagination
        const startIndex = (pageNumber - 1) * size;
        const endIndex = Math.min(startIndex + size, data.length);
        const pageData = data.slice(startIndex, endIndex);
        return { page: pageNumber, size, totalPages, data: pageData };
    } catch (error) {
        console.error(error);
        throw error; // Rethrow the error to be handled by the caller
    }
};


export const useGetFilteredBooksQueryMock = (filters: IBookFilters, page: number) : UseQueryResult<{ page: number; size: number; data: IBook[] }, Error> => {
    return useQuery({queryKey: [filters], queryFn: () => getFilteredBooksMock(filters, page)})
}

export default useGetFilteredBooksQuery;