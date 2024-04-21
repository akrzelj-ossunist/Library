import { useQuery } from "@tanstack/react-query";
import axios from "axios";

const getSearchedAuthor = async (val: string) => {
    try {
        const response = await axios.get("http://localhost:8081/api/v1/author/search", {
            params: {
                search: val
            }
        })
        return response.data;
    } catch (error) {
        console.error(error);
    }
}

const useGetSearchedAuthorQuery = (val: string) => {
    return useQuery({queryKey: [val], queryFn: () => getSearchedAuthor(val)})
}

export default useGetSearchedAuthorQuery;