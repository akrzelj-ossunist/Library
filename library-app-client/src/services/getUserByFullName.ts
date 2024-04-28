import { useQuery } from "@tanstack/react-query";
import axios from "axios";

const getUserByFullName = async (val: string) => {
  try {
    const response = await axios.get(
      "http://localhost:8081/api/v1/user/search",
      {
        params: {
          search: val,
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error(error);
  }
};

const useGetUserByFullNameQuery = (val: string) => {
  return useQuery({ queryKey: [val], queryFn: () => getUserByFullName(val) });
};

export default useGetUserByFullNameQuery;
