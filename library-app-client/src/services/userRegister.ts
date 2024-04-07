import axios from "axios";
import { IRegisterForm } from "../util/interface";

export const userRegister = async (registerForm: IRegisterForm) => {
    try {
        const response = await axios.post("http://localhost:8081/api/v1/user/register", registerForm, {
            headers: { 'Content-Type': 'application/json' }
        });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};
