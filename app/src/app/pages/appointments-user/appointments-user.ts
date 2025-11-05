import { Component, OnInit, resource, Resource } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { environment } from '../../../environment';
import { AppointmentResponseDTO } from '../../interfaces/appointmentDTO';

@Component({
  selector: 'app-appointments-user',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './appointments-user.html',
  styleUrl: './appointments-user.css',
})
export class AppointmentsUser implements OnInit {

  currentUser: any = null;

  appointments: Resource<AppointmentResponseDTO[]> = resource({
    loader: () => {
      if (!this.currentUser?.cedula) {
        return Promise.resolve([]);
      }
      return fetch(`${environment.apiUrl}/users/${this.currentUser.cedula}/appointments`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json'
        }
      }).then(result => result.json());
    }
  });

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUser();
  }

}
