import { Component, CUSTOM_ELEMENTS_SCHEMA, resource } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { environment } from '../../../environment';

@Component({
  selector: 'app-register-doctor',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './register-doctor.html',
  styleUrl: './register-doctor.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RegisterDoctor {

  showPassword = false;

  registerForm: FormGroup;

  specialties = resource({
    loader: () => fetch(`${environment.apiUrl}/specialities`).then(result => result.json())
  });

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      cedula: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
      specialityName: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    if (this.registerForm.valid) {
      // Handle form submission
      console.log(this.registerForm.value);
    }
  }

}