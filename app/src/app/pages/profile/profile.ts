import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { UserResponseDTO, UserUpdateDTO } from '../../interfaces/userDTO'; // Asegúrate que la ruta sea correcta

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class Profile implements OnInit {

  // --- Modelos de Datos ---
  // Datos que se cargan (para mostrar info estática como la cédula)
  currentUser: UserResponseDTO | null = null;
  
  // Datos enlazados al formulario (para actualizar)
  userUpdateData: UserUpdateDTO = {
    name: '',
    email: '',
    password: '', // Empezar vacío
    epsName: 0,   // ID de la EPS
    cityName: 0,  // ID de la Ciudad
    phones: ['']  // Empezar con un teléfono
  };

  // --- Listas para Desplegables ---
  ciudadesList: any[] = [];
  epsList: any[] = [];
  cargandoCiudades = false;
  cargandoEps = false;

  constructor() { }

  ngOnInit(): void {
    this.cargarListas();
    this.cargarPerfilSimulado();
  }

  // --- Carga de Datos (Simulada) ---

  cargarListas(): void {
    this.cargarCiudades();
    this.cargarEps();
  }

  cargarCiudades(): void {
    this.cargandoCiudades = true;
    const mockCiudadesApiCall = of([
      { id: 1, nombre: 'Armenia' },
      { id: 2, nombre: 'Bogotá' },
      { id: 3, nombre: 'Medellín' },
      { id: 4, nombre: 'Cali' }
    ]).pipe(delay(1000)); 

    mockCiudadesApiCall.subscribe(data => {
      this.ciudadesList = data;
      this.cargandoCiudades = false;
    });
  }

  cargarEps(): void {
    this.cargandoEps = true;
    const mockEpsApiCall = of([
      { id: 1, nombre: 'Sura' },
      { id: 2, nombre: 'Sana' },
      { id: 3, nombre: 'Compensar' },
      { id: 4, nombre: 'Nueva EPS' }
    ]).pipe(delay(1500)); 

    mockEpsApiCall.subscribe(data => {
      this.epsList = data;
      this.cargandoEps = false;
    });
  }

  /**
   * Simula la carga del perfil del usuario actual.
   */
  cargarPerfilSimulado(): void {
    // Simula una respuesta de UserResponseDTO
    const mockProfileData: UserResponseDTO = {
      cedula: '1094123456',
      name: 'Usuario de Prueba',
      email: 'usuario@prueba.com',
      epsName: 'Sura',       // Nombre (string)
      cityName: 'Armenia',  // Nombre (string)
      phones: ['3111234567']
    };

    // Simula la llamada
    of(mockProfileData).pipe(delay(500)).subscribe(data => {
      this.currentUser = data;

      // Mapea los datos al formulario de actualización (UserUpdateDTO)
      this.userUpdateData = {
        name: data.name,
        email: data.email,
        password: '', // Contraseña siempre vacía al cargar
        phones: data.phones.length > 0 ? data.phones : [''],
        // Asignamos los IDs basándonos en los nombres (simulación)
        // En un caso real, la API de perfil ya debería devolver los IDs
        epsName: 1,   // Asumimos que 'Sura' es el ID 1
        cityName: 1 // Asumimos que 'Armenia' es el ID 1
      };
    });
  }


  // --- Lógica del Formulario ---

  /**
   * Se llama al enviar el formulario.
   */
  actualizarPerfil(): void {
    console.log('Enviando actualización:', this.userUpdateData);
    // Aquí iría la llamada al servicio:
    // this.authService.updateProfile(this.userUpdateData).subscribe(...)
  }

  // --- Lógica de Teléfonos ---

  agregarTelefono(): void {
    this.userUpdateData.phones.push('');
  }

  eliminarTelefono(index: number): void {
    if (this.userUpdateData.phones.length > 1) {
      this.userUpdateData.phones.splice(index, 1);
    }
  }
}
