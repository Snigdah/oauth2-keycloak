import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { KeycloakService } from './auth/keycloak.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('public-client');

  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}
}
