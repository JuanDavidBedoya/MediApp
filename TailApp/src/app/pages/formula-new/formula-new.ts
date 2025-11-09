import { Component, OnInit, resource, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { FormulaService } from '../../services/formula-service';
import { AuthService } from '../../services/auth';
import { FormulaRequestDTO, FormulaItemDTO } from '../../interfaces/formulaDTO';
import { MedicationDTO } from '../../interfaces/medicationDTO';

@Component({
    selector: 'app-formula-new',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './formula-new.html'
})
export class FormulaNew implements OnInit {
    private formulaService = inject(FormulaService);
    private authService = inject(AuthService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);

    doctorCedula: string | null = null;
    patientCedula: string | null = null;
    appointmentId: string | null = null;

    formulaData: Partial<FormulaRequestDTO> = {
        diagnostic: '',
    };

    formulaItems: FormulaItemDTO[] = [
        { medicationName: '', quantity: '', instructions: '' },
    ];

    medicationsResource = resource({
        loader: () => (this.formulaService.getMedications().toPromise() as Promise<MedicationDTO[]>),
        defaultValue: [] as MedicationDTO[]
    });

    isSubmitting = false;
    submissionMessage: { type: 'success' | 'error'; text: string } | null = null;
    isLoadingInitial = true;

    ngOnInit(): void {
        const doctor = this.authService.getUser();

        if (doctor && doctor.cedula && this.authService.isDoctor()) {
            this.doctorCedula = doctor.cedula;
        } else {
            this.submissionMessage = { type: 'error', text: 'Acceso denegado. Solo doctores pueden crear prescripciones.' };
            this.isLoadingInitial = false;
            return;
        }

        this.route.queryParams.subscribe(params => {
            this.patientCedula = params['patientCedula'] || null;
            this.appointmentId = params['appointmentId'] || null;

            if (!this.patientCedula || !this.appointmentId) {
                this.submissionMessage = { type: 'error', text: 'Faltan datos de paciente o cita. No se puede crear la prescripción.' };
                this.isLoadingInitial = false;
                return;
            }

            this.isLoadingInitial = false;
        });
    }

    agregarItem(): void {
        this.formulaItems.push({ medicationName: '', quantity: '', instructions: '' });
    }

    eliminarItem(index: number): void {
        if (this.formulaItems.length > 1) {
            this.formulaItems.splice(index, 1);
        }
    }

    trackByIndex(index: number, item: any): any {
        return index;
    }

    createFormula(): void {
        if (this.isSubmitting || !this.doctorCedula || !this.patientCedula || !this.appointmentId) return;

        const validItems = this.formulaItems.filter(
            item => item.medicationName && item.medicationName.trim() !== ''
        );

        if (!this.formulaData.diagnostic || validItems.length === 0) {
            this.submissionMessage = { type: 'error', text: 'Debes ingresar un diagnóstico y al menos un medicamento válido.' };
            return;
        }

        this.isSubmitting = true;
        this.submissionMessage = null;

        const payload: FormulaRequestDTO = {
            doctorCedula: this.doctorCedula,
            patientCedula: this.patientCedula,
            appointmentId: this.appointmentId,
            diagnostic: this.formulaData.diagnostic!,
            formulaItems: validItems,
        };

        this.formulaService.createFormula(payload).subscribe(
            (response: any) => {
                this.isSubmitting = false;
                this.submissionMessage = {
                    type: 'success',
                    text: `¡Prescripción No. ${response.idFormula} creada con éxito!`,
                };
            },
            (err: HttpErrorResponse) => {
                this.isSubmitting = false;
                const errorMsg =
                    err.error?.message ||
                    'Hubo un error al crear la prescripción. Verifica los datos o el estado de la cita.';
                this.submissionMessage = { type: 'error', text: errorMsg };
            }
        );
    }
}