import { Component, OnInit } from '@angular/core';
import { KeycloakService } from '../auth/keycloak.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { JsonPipe } from '@angular/common';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [JsonPipe, CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  token: string = '';
  getResponse: any = null;
  postResponse: any = null;
  deleteResponse: any = null;
  loading = false;

  constructor(private keycloakService: KeycloakService, private http: HttpClient) { }

  ngOnInit(): void {
    this.token = this.keycloakService.getToken();
  }

  logout(): void {
    this.keycloakService.logout();
  }

   private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.keycloakService.getToken()}`
    });
  }

  callGetApi(): void {
    this.loading = true;
    this.http.get(`${environment.apiBaseUrl}/product`, { headers: this.getHeaders() })
      .subscribe({
        next: res => {
          this.getResponse = res;
          this.loading = false;
        },
        error: err => {
          this.getResponse = err.error || err.message;
          this.loading = false;
        }
      });
  }

  callPostApi(): void {
    this.loading = true;
    this.http.post(`${environment.apiBaseUrl}/product`, {}, { headers: this.getHeaders() })
      .subscribe({
        next: res => {
          this.postResponse = res;
          this.loading = false;
        },
        error: err => {
          this.postResponse = err.error || err.message;
          this.loading = false;
        }
      });
  }

  callDeleteApi(): void {
    this.loading = true;
    this.http.delete(`${environment.apiBaseUrl}/product/1`, { headers: this.getHeaders() })
      .subscribe({
        next: res => {
          this.deleteResponse = res;
          this.loading = false;
        },
        error: err => {
          this.deleteResponse = err.error || err.message;
          this.loading = false;
        }
      });
  }
}
