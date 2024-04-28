import { JwtPayload } from "jwt-decode";

export interface IUser {
  id: string;
  fullName: string;
  email: string;
  address: string;
  birthday: Date;
  role: string;
}

export interface ILoginCredentials {
  success: boolean;
  user: IUser;
  jwtToken: string;
}

export interface ILoginForm {
  email: string;
  password: string;
}

export interface IRegisterForm {
  fullName: string;
  email: string;
  emailRepeat: string;
  password: string;
  passwordRepeat: string;
  address: string;
  birthday: Date;
}

export interface IJwtPayload extends JwtPayload {
  scope: string;
}

export interface IBookFilters {
  genre: string | (string | null)[] | null;
  available: boolean | null;
  book: string | (string | null)[] | null;
  isbn: string | (string | null)[] | null;
  author: string | (string | null)[] | null;
}

export interface IBook {
  id: string;
  title: string;
  author: {
    id: number;
    fullName: string;
    birthday: string; // Assuming this is a string representing the birthday
  };
  createdDate: string; // Assuming this is a string representing the creation date
  genre: string;
  isAvailable: boolean;
  isbn: string;
}
