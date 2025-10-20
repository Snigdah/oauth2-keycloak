import { Component } from '@angular/core';
import { KeycloakService } from '../auth/keycloak.service';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  // constructor(private keycloakService: KeycloakService) { }

  // login(): void {
  //   this.keycloakService.login();
  // }

  isLoading = false;

  constructor(private keycloakService: KeycloakService) { }

  async login(): Promise<void> {
    this.isLoading = true;

    await this.keycloakService.login();
  }
}
