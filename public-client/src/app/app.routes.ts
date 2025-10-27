import { RouterModule, Routes } from '@angular/router';
import { Home } from './home/home';
import { Login } from './login/login';
import { NgModule } from '@angular/core';
import { Landing } from './landing/landing';
import { About } from './about/about';
import { Registration } from './registration/registration';

export const routes: Routes = [
    { path: '', redirectTo: '/landing', pathMatch: 'full' },
    { path: 'home', component: Home },
    { path: 'about', component: About },
    { path: 'registration', component: Registration },
    { path: 'login', component: Login },
    { path: 'landing', component: Landing },
    // { path: '**', redirectTo: '/login' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }