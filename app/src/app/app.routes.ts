import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { HomePublic } from './pages/home-public/home-public';
import { Register } from './pages/register/register';
import { HomePrivate } from './pages/home-private/home-private';
import { Profile } from './pages/profile/profile';
import { HomeDoctor } from './pages/home-doctor/home-doctor';
import { RegisterDoctor } from './pages/register-doctor/register-doctor';
import { AppointmentsUser } from './pages/appointments-user/appointments-user';

export const routes: Routes = [
    { path: '', redirectTo: 'home-public', pathMatch: 'full' },
    { path: 'login', component: Login},
    { path: 'home-public', component: HomePublic},
    { path: 'home-doctor', component: HomeDoctor},
    { path: 'home-private', component: HomePrivate},
    { path: 'register', component: Register},
    { path: 'profile', component: Profile},
    { path: 'register-doctor', component: RegisterDoctor},
    { path: 'appointments-user', component: AppointmentsUser},
    { path: '**', redirectTo: 'home-public' },
];
