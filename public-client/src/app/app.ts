import { Component, OnInit, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { KeycloakService } from './auth/keycloak.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('public-client');

  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) { }

  async ngOnInit(): Promise<void> {
    const isAuthenticated = await this.keycloakService.init();

    const currentPath = window.location.pathname;

    if (isAuthenticated) {
      if (currentPath === '/login' || currentPath === '/') {
        this.router.navigate(['/home']);
      }
    } else {
      if (currentPath !== '/login') {
        this.router.navigate(['/login']);
      }
    }
  }
}
