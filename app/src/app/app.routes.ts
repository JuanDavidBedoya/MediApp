import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { HomePublic } from './pages/home-public/home-public';
import { Register } from './pages/register/register';
import { HomePrivate } from './pages/home-private/home-private';

export const routes: Routes = [
    { path: '', redirectTo: 'home-public', pathMatch: 'full' },
    { path: 'login', component: Login},
    { path: 'home-public', component: HomePublic},
    { path: 'register', component: Register},
    { path: 'home-private', component: HomePrivate},
    { path: '**', redirectTo: 'home-public' },
];
