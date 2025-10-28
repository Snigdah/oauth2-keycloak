import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  // ✅ Product API message state
  apiMessage: string = '';
  apiStatus: 'success' | 'error' | '' = '';

  // ✅ Order API message state
  orderApiMessage: string = '';
  orderApiStatus: 'success' | 'error' | '' = '';

  private productApiUrl = 'http://localhost:8087/api/product';
  private orderApiUrl = 'http://localhost:8088/api/order';

  constructor(private keycloakService: KeycloakService, private http: HttpClient) {}

  ngOnInit(): void {
    this.token = this.keycloakService.getToken();
    this.loadUserInfo();
    this.setLastLoginTime();
  }

  loadUserInfo(): void {
    const keycloak = this.keycloakService.keycloak;
    keycloak.loadUserProfile().then((profile) => {
      this.username = profile.firstName && profile.lastName
        ? `${profile.firstName} ${profile.lastName}`
        : profile.username || 'User';
      this.email = profile.email || '';
    }).catch(() => {
      this.username = keycloak.tokenParsed?.['preferred_username'] || 'User';
      this.email = keycloak.tokenParsed?.['email'] || '';
    });

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

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.token}`
    });
  }

  // ====================================================
  // ✅ PRODUCT API CALLS
  // ====================================================

  testGet(): void {
    this.http.get(this.productApiUrl, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (res) => this.showSuccess(res),
        error: (err) => this.showError(err)
      });
  }

  testPost(): void {
    this.http.post(this.productApiUrl, {}, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (res) => this.showSuccess(res),
        error: (err) => this.showError(err)
      });
  }

  testDelete(): void {
    this.http.delete(`${this.productApiUrl}/1`, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (res) => this.showSuccess(res),
        error: (err) => this.showError(err)
      });
  }

  // ====================================================
  // ✅ ORDER API CALLS
  // ====================================================

  testOrderGet(): void {
    this.http.get(`${this.orderApiUrl}`, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (res) => this.showOrderSuccess(res),
        error: (err) => this.showOrderError(err)
      });
  }

  testOrderPost(): void {
    this.http.post(this.orderApiUrl, {}, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (res) => this.showOrderSuccess(res),
        error: (err) => this.showOrderError(err)
      });
  }

  testOrderDelete(): void {
    this.http.delete(`${this.orderApiUrl}/1`, { headers: this.getHeaders(), responseType: 'text' })
      .subscribe({
        next: (res) => this.showOrderSuccess(res),
        error: (err) => this.showOrderError(err)
      });
  }

  // ====================================================
  // ✅ PRODUCT API HELPERS
  // ====================================================
  private showSuccess(msg: string): void {
    this.apiStatus = 'success';
    this.apiMessage = msg;
    setTimeout(() => this.clearMessage(), 3000);
  }

  private showError(err: any): void {
    this.apiStatus = 'error';
    this.apiMessage = this.formatError(err);
    setTimeout(() => this.clearMessage(), 3000);
  }

  private clearMessage(): void {
    this.apiMessage = '';
    this.apiStatus = '';
  }

  // ====================================================
  // ✅ ORDER API HELPERS
  // ====================================================
  private showOrderSuccess(msg: string): void {
    this.orderApiStatus = 'success';
    this.orderApiMessage = msg;
    setTimeout(() => this.clearOrderMessage(), 3000);
  }

  private showOrderError(err: any): void {
    this.orderApiStatus = 'error';
    this.orderApiMessage = this.formatError(err);
    setTimeout(() => this.clearOrderMessage(), 3000);
  }

  private clearOrderMessage(): void {
    this.orderApiMessage = '';
    this.orderApiStatus = '';
  }

  // ✅ Common error formatter
  private formatError(err: any): string {
    if (err.status === 401 || err.status === 403) {
      return 'Unauthorized ❌';
    } else {
      return `Error: ${err.statusText || 'Unknown error'}`;
    }
  }
}
