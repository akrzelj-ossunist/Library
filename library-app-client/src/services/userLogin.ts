import axios from "axios";
import { ILoginForm } from "../util/interface";

export const userLogin = async (loginForm: ILoginForm) => {
    try {
        const response = await axios.post("http://localhost:8081/api/v1/user/login", loginForm, {
            headers: { 'Content-Type': 'application/json' }
        });
        return response.data;
    } catch (error) {
        console.error(error);
        return error;
    }
};
