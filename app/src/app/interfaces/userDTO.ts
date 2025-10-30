export interface UserResponseDTO {
  cedula: string;
  name: string;
  email: string;
  epsName: string;
  phones: string[];
  cityName: string;
}

export interface UserRequestDTO {
  cedula: string;
  name: string;
  email: string;
  password: string;
  epsName: number;
  phones: string[];
  cityName: number;
}

export interface UserUpdateDTO {
  name: string;
  email: string;
  password: string;
  epsName: number;
  cityName: number;
  phones: string[];
}
