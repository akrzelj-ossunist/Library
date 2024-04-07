import { useQuery } from "@tanstack/react-query";
import axios from "axios"

const getUserById = async (userId: string) => {
    try {
        const response = await axios.get(`http://localhost:8081/api/v1/user/profile/${userId}`);
        return response.data;
    } catch (error) {
        console.error(error)
    }
}

const useGetUserByIdQuery = (userId: string) => {
    return useQuery({queryKey: [userId], queryFn: () => getUserById(userId)});
}

export default useGetUserByIdQuery;