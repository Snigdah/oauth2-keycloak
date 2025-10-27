import { Component, OnInit } from '@angular/core';
import { KeycloakService } from '../auth/keycloak.service';
import { Navbar } from '../navbar/navbar';

@Component({
  selector: 'app-home',
  imports: [Navbar],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  token: string = '';

  constructor(private keycloakService: KeycloakService) { }

  ngOnInit(): void {
    this.token = this.keycloakService.getToken();
  }

  logout(): void {
    this.keycloakService.logout();
  }
}
