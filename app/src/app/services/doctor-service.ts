import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { DoctorRequestDTO, DoctorResponseDTO } from '../interfaces/doctorDTO';

@Injectable({
  providedIn: 'root',
})
export class DoctorService {

  private apiUrl = `${environment.apiUrl}/auth/register/doctor`;

  constructor(private http: HttpClient) {}

  registrarDoctor(
    doctor: DoctorRequestDTO
  ): Observable<DoctorResponseDTO> {
    return this.http.post<DoctorResponseDTO>(this.apiUrl, doctor);
  }

}