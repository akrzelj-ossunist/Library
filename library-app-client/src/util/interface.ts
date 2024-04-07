import { JwtPayload } from "jwt-decode"

export interface IUser {
  id: string
  fullName: string
  email: string
  address: string
  birthday: Date
  role: string
}

export interface ILoginCredentials {
    success: boolean
    user: IUser
    jwtToken: string
}

export interface ILoginForm {
  email: string
  password: string
}

export interface IRegisterForm {
  fullName: string
  email: string
  emailRepeat: string
  password: string
  passwordRepeat: string
  address: string
  birthday: Date
}

export interface IJwtPayload extends JwtPayload {
  scope: string
}
