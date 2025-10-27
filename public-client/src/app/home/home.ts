import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeycloakService } from '../auth/keycloak.service';
import { Navbar } from '../navbar/navbar';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [Navbar, CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  token: string = '';
  username: string = '';
  email: string = '';
  lastLoginTime: string = '';
  sessionActive: boolean = true;
  protectionEnabled: boolean = true;

  constructor(private keycloakService: KeycloakService) { }

  ngOnInit(): void {
    this.token = this.keycloakService.getToken();
    this.loadUserInfo();
    this.setLastLoginTime();
  }

  loadUserInfo(): void {
    const keycloak = this.keycloakService.keycloak;
    
    // Load user profile
    keycloak.loadUserProfile().then((profile) => {
      this.username = profile.firstName && profile.lastName 
        ? `${profile.firstName} ${profile.lastName}`
        : profile.username || 'User';
      this.email = profile.email || '';
    }).catch(() => {
      this.username = keycloak.tokenParsed?.['preferred_username'] || 'User';
      this.email = keycloak.tokenParsed?.['email'] || '';
    });

    // Check session status
    this.sessionActive = this.keycloakService.authenticated;
    this.protectionEnabled = keycloak.authenticated || false;
  }

  setLastLoginTime(): void {
    const now = new Date();
    const hours = now.getHours();
    const minutes = now.getMinutes();
    const ampm = hours >= 12 ? 'PM' : 'AM';
    const displayHours = hours % 12 || 12;
    const displayMinutes = minutes < 10 ? '0' + minutes : minutes;
    
    this.lastLoginTime = `${displayHours}:${displayMinutes} ${ampm}`;
  }

  logout(): void {
    this.keycloakService.logout();
  }
}