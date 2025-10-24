import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home-public',
  imports: [RouterModule, CommonModule],
  templateUrl: './home-public.html',
  styleUrl: './home-public.css',
})
export class HomePublic {

}
