import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { HomePublic } from './pages/home-public/home-public';
import { Register } from './pages/register/register';

export const routes: Routes = [
    { path: 'login', component: Login},
    { path: 'home-public', component: HomePublic},
    { path: 'register', component: Register},
];
