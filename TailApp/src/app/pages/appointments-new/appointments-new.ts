import { Component, OnInit, resource, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AppointmentService } from '../../services/appointment-service';
import { AuthService } from '../../services/auth';
import { AppointmentRequestDTO } from '../../interfaces/appointmentDTO';
import { SpecialityResponseDTO } from '../../interfaces/specialityDTO';
import { DoctorResponseDTO } from '../../interfaces/doctorDTO';

@Component({
    selector: 'app-appointments-new',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './appointments-new.html'
})
export class AppointmentsNew implements OnInit {

    private appointmentService = inject(AppointmentService);
    private authService = inject(AuthService);
    private router = inject(Router);

    selectedSpecialityName = signal<string | undefined>(undefined);
    selectedDoctorCedula = signal<string | undefined>(undefined);
    selectedDate = signal<string | undefined>(undefined);
    selectedTime = signal<string | undefined>(undefined);

    appointmentData: Partial<AppointmentRequestDTO> = {
        patientCedula: '',
        observations: '',
    };

    isSubmitting = false;
    submissionMessage: { type: 'success' | 'error'; text: string } | null = null;
    isLoadingInitial = true;

    minDate: string = new Date().toISOString().split('T')[0];

    specialitiesResource = resource({
        loader: async () => {
            try {
                return await this.appointmentService.getSpecialities().toPromise() as SpecialityResponseDTO[];
            } catch (error) {
                console.error('Error cargando especialidades:', error);
                this.submissionMessage = { type: 'error', text: 'Error al cargar especialidades médicas.' };
                return [];
            }
        },
        defaultValue: [] as SpecialityResponseDTO[]
    });

    doctorsResource = resource({
        params: () => ({ speciality: this.selectedSpecialityName() }),
        loader: ({ params }) => {
            const speciality = params.speciality;
            if (!speciality) return Promise.resolve([]);
            return this.appointmentService.getDoctorsBySpeciality(speciality!).toPromise() as Promise<DoctorResponseDTO[]>;
        },
        defaultValue: [] as DoctorResponseDTO[]
    });

    availableSlotsResource = resource({
        params: () => ({
            doctorCedula: this.selectedDoctorCedula(),
            date: this.selectedDate()
        }),
        loader: ({ params }) => {
            const { doctorCedula, date } = params;
            if (!doctorCedula || !date) return Promise.resolve([]);
            return this.appointmentService.getAvailableSlots(doctorCedula, date).toPromise() as Promise<string[]>;
        },
        defaultValue: [] as string[]
    });


    ngOnInit(): void {
        const user = this.authService.getUser();
        if (user && user.cedula) {
            this.appointmentData.patientCedula = user.cedula;
            this.isLoadingInitial = false;
        } else {
            this.submissionMessage = { type: 'error', text: 'No se pudo identificar al usuario. Inicia sesión.' };
            this.isLoadingInitial = false;
            setTimeout(() => this.router.navigate(['/login']), 2000);
        }
    }

    onSpecialityChange(event: Event): void {
        const specialityName = (event.target as HTMLSelectElement).value;
        this.selectedSpecialityName.set(specialityName);
        this.selectedDoctorCedula.set(undefined);
        this.selectedDate.set(undefined);
        this.selectedTime.set(undefined);
    }

    onDoctorChange(event: Event): void {
        const doctorCedula = (event.target as HTMLSelectElement).value;
        this.selectedDoctorCedula.set(doctorCedula);
        this.selectedDate.set(undefined);
        this.selectedTime.set(undefined);
    }

    onDateChange(event: Event): void {
        const date = (event.target as HTMLInputElement).value;
        this.selectedDate.set(date);
        this.selectedTime.set(undefined);
    }

    onTimeChange(event: Event): void {
        this.selectedTime.set((event.target as HTMLSelectElement).value);
    }

    scheduleAppointment(): void {
        if (this.isSubmitting || !this.selectedDoctorCedula() || !this.selectedDate() || !this.selectedTime()) {
            this.submissionMessage = { type: 'error', text: 'Por favor, completa los campos de Especialidad, Doctor, Fecha y Hora.' };
            return;
        }

        this.isSubmitting = true;
        this.submissionMessage = null;

        const payload: AppointmentRequestDTO = {
            patientCedula: this.appointmentData.patientCedula!,
            doctorCedula: this.selectedDoctorCedula()!,
            date: this.selectedDate()!,
            time: this.selectedTime()!,
            observations: this.appointmentData.observations || '',
        };

        this.appointmentService.scheduleAppointment(payload).subscribe({
            next: (response) => {
                this.isSubmitting = false;

                const doctorName = this.doctorsResource.value().find(d => d.cedula === response.doctorCedula)?.name || 'el doctor';

                this.submissionMessage = {
                    type: 'success',
                    text: `¡Cita agendada con éxito con ${doctorName} el ${response.date} a las ${response.time}! Redirigiendo...`,
                };
                setTimeout(() => this.router.navigate(['/appointments-user']), 3000);
            },
            error: (err: HttpErrorResponse) => {
                this.isSubmitting = false;
                const errorMsg =
                    err.error?.message ||
                    'Hubo un error al agendar la cita. Verifica que el doctor tenga disponibilidad.';
                this.submissionMessage = { type: 'error', text: errorMsg };
            },
        });
    }
}