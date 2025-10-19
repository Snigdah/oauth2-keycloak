import { inject } from '@angular/core';
import { CanActivate, CanActivateFn, Router } from '@angular/router';
import { KeycloakService } from './keycloak.service';

export class authGuard implements CanActivate {
    constructor(
        private keycloakService: KeycloakService,
        private router: Router
    ) { }

    canActivate(): boolean {
        if (this.keycloakService.authenticated) {
            return true;
        } else {
            this.router.navigate(['/login']);
            return false;
        }
    }
}
