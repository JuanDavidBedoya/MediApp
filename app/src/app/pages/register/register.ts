import { Component, CUSTOM_ELEMENTS_SCHEMA, resource } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { register } from 'swiper/element/bundle';
import { environment } from '../../../environment';

register();

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Register {

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

  cities = resource({
    loader: () => fetch(`${environment.apiUrl}/cities`).then(result => result.json())
  });

  epsList = resource({
    loader: () => fetch(`${environment.apiUrl}/eps`).then(result => result.json())
  });

}