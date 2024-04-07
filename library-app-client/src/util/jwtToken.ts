import { jwtDecode } from "jwt-decode";
import { IJwtPayload } from "./interface";

const decodeJwtToken = (token: string): IJwtPayload | null => {
    try {
        if (token) {
            return jwtDecode<IJwtPayload>(token);
        } else {
            throw new Error('Token is empty');
        }
    } catch (error) {
        return null;
    }
};

export default decodeJwtToken;