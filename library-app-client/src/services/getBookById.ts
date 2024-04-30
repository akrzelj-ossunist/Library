import { useQuery } from "@tanstack/react-query";
import axios from "axios";

const getBookById = async (bookId: string) => {
  try {
    const response = await axios.get(
      `http://localhost:8081/api/v1/book/${bookId}`
    );
    return response.data;
  } catch (error) {
    console.error(error);
  }
};

const useGetBookByIdQuery = (bookId: string) => {
  return useQuery({ queryKey: [bookId], queryFn: () => getBookById(bookId) });
};

export default useGetBookByIdQuery;
