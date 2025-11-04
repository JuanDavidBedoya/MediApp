import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { UserRequestDTO, UserResponseDTO, UserUpdateDTO } from '../interfaces/userDTO';


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiUrl = `${environment.apiUrl}/auth/register`;

  constructor(private http: HttpClient) {}

  registrarUsuario(usuario: UserRequestDTO): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(this.apiUrl, usuario);
  }

  getUsuario(cedula: string): Observable<UserResponseDTO> {
    return this.http.get<UserResponseDTO>(`${this.apiUrl}/${cedula}`);
  }

  actualizarUsuario(cedula: string, usuario: UserUpdateDTO): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>(`${this.apiUrl}/${cedula}`, usuario);
  }
}
