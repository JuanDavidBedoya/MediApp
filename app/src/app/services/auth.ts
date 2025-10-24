import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environment';
import { AuthResponseDTO } from '../interfaces/auth';
import { LoginDTO } from '../interfaces/loginDTO';
import { UserResponseDTO } from '../interfaces/userDTO';
import { DoctorResponseDTO } from '../interfaces/doctorDTO';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = environment.apiUrl || '';

  constructor(private http: HttpClient) { }

  login(request: LoginDTO): Observable<AuthResponseDTO> {
    const url = `${this.baseUrl}/auth/login`;
    
    return this.http.post<AuthResponseDTO>(url, request).pipe(
      tap(response => {

        console.log('Respuesta de autenticación recibida en el servicio:', response);

      })
    );
  }

  saveSession(response: AuthResponseDTO): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('role', response.role);
    localStorage.setItem('user', JSON.stringify(response.user));
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('user');
    // Falta redirigir al login
  }


  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getUser(): UserResponseDTO | DoctorResponseDTO | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
}
