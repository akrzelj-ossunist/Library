import decodeJwtToken from "./jwtToken";

export const validateJwtToken = () => {
    const jwtToken = localStorage.getItem("jwt") || "";
    if (jwtToken) {
        try {
            if (decodeJwtToken(jwtToken)?.exp! < Date.now() / 1000) {
                console.error('JWT token has expired');
                return null;
            }
            return jwtToken;
        } catch (error) {
            console.error('Invalid JWT token:', error);
            return null;
        }
    } else {
        console.error('JWT token not found in localStorage');
        return null; 
    }
}