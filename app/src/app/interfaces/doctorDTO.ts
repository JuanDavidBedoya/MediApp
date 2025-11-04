export interface DoctorResponseDTO {
  cedula: string;
  name: string;
  phone: string;
  email: string;
  specialityName: string;
}

export interface DoctorRequestDTO {
  cedula: string;
  name: string;
  phone: string;
  email: string;
  specialityName: string;
  password: string;
}
