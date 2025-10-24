import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { register } from 'swiper/element/bundle';

register();

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.css',
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})

export class Login {

}
