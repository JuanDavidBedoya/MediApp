import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { FormulaRequestDTO, FormulaResponseDTO } from '../interfaces/formulaDTO';
import { MedicationDTO } from '../interfaces/medicationDTO';

@Injectable({
    providedIn: 'root',
})
export class FormulaService {
    private apiUrl = `${environment.apiUrl}`;
    private formulaApiUrl = `${this.apiUrl}/formulas`;
    private medicationsApiUrl = `${this.apiUrl}/medications`;

    constructor(private http: HttpClient) { }

    getMedications(): Observable<MedicationDTO[]> {
        return this.http.get<MedicationDTO[]>(this.medicationsApiUrl);
    }

    createFormula(
        request: FormulaRequestDTO
    ): Observable<FormulaResponseDTO> {
        return this.http.post<FormulaResponseDTO>(
            this.formulaApiUrl,
            request
        );
    }
}