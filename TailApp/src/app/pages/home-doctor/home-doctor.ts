import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth'; // Asegúrate que la ruta sea correcta

@Component({
  selector: 'app-home-doctor',
  standalone: true,
  imports: [CommonModule, RouterLink], // Añade CommonModule y RouterLink
  templateUrl: './home-doctor.html'
})
export class HomeDoctor {

  // 1. Inyecta el AuthService
  constructor(private authService: AuthService) { }

  // 2. Añade los getters para que el HTML pueda usarlos
  get userName(): string | null {
    const user = this.authService.getUser();
    // Asumimos que la respuesta de 'user' tiene una propiedad 'name' o 'nombre'
    // Ajusta 'name' si la propiedad se llama diferente (ej: 'nombre')
    return user ? (user as any).name || (user as any).nombre : null; 
  }

  get userRole(): string | null {
    return this.authService.getRole();
  }
  
}