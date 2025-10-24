import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; 
import { FormsModule } from '@angular/forms'; 
import { register } from 'swiper/element/bundle';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

register();

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink], 
  templateUrl: './register.html',
  styleUrl: './register.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA] 
})
export class Register implements OnInit {

  showPassword = false;

  telefonos: string[] = [''];

  agregarTelefono(): void {
    this.telefonos.push('');
  }

  eliminarTelefono(index: number): void {
    if (this.telefonos.length > 1) {
      this.telefonos.splice(index, 1);
    }
  }

  trackByIndex(index: number, item: any): any {
    return index;
  }

  ciudades: any[] = [];
  epsList: any[] = [];

  cargandoCiudades = false;
  cargandoEps = false;

  ngOnInit(): void {
    this.cargarDatosSimulados();
  }


  cargarDatosSimulados(): void {
    this.simularGetCiudades();
    this.simularGetEps();
  }

  simularGetCiudades(): void {
    this.cargandoCiudades = true;

    const mockCiudadesApiCall = of<any[]>([
      { id: 1, nombre: 'Armenia' },
      { id: 2, nombre: 'Bogotá' },
      { id: 3, nombre: 'Medellín' },
      { id: 4, nombre: 'Cali' }
    ]).pipe(delay(1000)); 

    mockCiudadesApiCall.subscribe(data => {
      this.ciudades = data;
      this.cargandoCiudades = false;
    });
  }

  simularGetEps(): void {
    this.cargandoEps = true;

    const mockEpsApiCall = of<any[]>([
      { id: 'eps-01', nombre: 'Sura' },
      { id: 'eps-02', nombre: 'Sana' },
      { id: 'eps-03', nombre: 'Compensar' },
      { id: 'eps-04', nombre: 'Nueva EPS' }
    ]).pipe(delay(1500)); 

    mockEpsApiCall.subscribe(data => {
      this.epsList = data;
      this.cargandoEps = false;
    });
  }

}