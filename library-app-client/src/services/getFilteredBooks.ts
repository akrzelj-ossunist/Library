import axios from "axios";
import { IBookFilters } from "../util/interface";
import { useInfiniteQuery } from "@tanstack/react-query";

const getFilteredBooks = async (filters: IBookFilters, page: number) => {
    console.log(page)
    try {
        const response = await axios.get(`http://localhost:8081/api/v1/book/query`, {
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

export default useGetFilteredBooksQuery;