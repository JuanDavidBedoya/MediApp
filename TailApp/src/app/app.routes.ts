import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { HomePublic } from './pages/home-public/home-public';
import { Register } from './pages/register/register';
import { HomePrivate } from './pages/home-private/home-private';
import { Profile } from './pages/profile/profile';
import { HomeDoctor } from './pages/home-doctor/home-doctor';
import { RegisterDoctor } from './pages/register-doctor/register-doctor';
import { AppointmentsUser } from './pages/appointments-user/appointments-user';
import { ProfileDoctor } from './pages/profile-doctor/profile-doctor';
import { AppointmentsNew } from './pages/appointments-new/appointments-new';
import { FormulaNew } from './pages/formula-new/formula-new';
import { AppointmentsDoctor } from './pages/appointments-doctor/appointments-doctor';

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
    { path: 'appointments-doctor', component: AppointmentsDoctor },
    { path: 'appointments-new', component: AppointmentsNew},
    { path: 'profile-doctor', component: ProfileDoctor},
    { path: 'create-formula', component: FormulaNew},
    { path: '**', redirectTo: 'home-public' },
];
