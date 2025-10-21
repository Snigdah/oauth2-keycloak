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
    this.getResponse = null;
    this.http.get(`${environment.apiBaseUrl}/product`, { 
      headers: this.getHeaders(),
      responseType: 'text'  // Expect text response
    })
      .subscribe({
        next: (res: string) => {
          this.getResponse = {
            success: true,
            message: res
          };
          this.loading = false;
        },
        error: err => {
          this.getResponse = this.formatError(err);
          this.loading = false;
        }
      });
  }

  callPostApi(): void {
    this.loading = true;
    this.postResponse = null;
    this.http.post(`${environment.apiBaseUrl}/product`, {}, { 
      headers: this.getHeaders(),
      responseType: 'text'  // Expect text response
    })
      .subscribe({
        next: (res: string) => {
          this.postResponse = {
            success: true,
            message: res
          };
          this.loading = false;
        },
        error: err => {
          this.postResponse = this.formatError(err);
          this.loading = false;
        }
      });
  }

  callDeleteApi(): void {
    this.loading = true;
    this.deleteResponse = null;
    this.http.delete(`${environment.apiBaseUrl}/product/1`, { 
      headers: this.getHeaders(),
      responseType: 'text'  // Expect text response
    })
      .subscribe({
        next: (res: string) => {
          this.deleteResponse = {
            success: true,
            message: res
          };
          this.loading = false;
        },
        error: err => {
          this.deleteResponse = this.formatError(err);
          this.loading = false;
        }
      });
  }

  private formatError(error: any): any {
    // For 401 Unauthorized errors
    if (error.status === 401) {
      return {
        success: false,
        status: error.status,
        error: error.error?.error || 'Unauthorized'
      };
    }
    
    // For other HTTP errors
    if (error.status) {
      return {
        success: false,
        status: error.status,
        error: this.getErrorMessage(error)
      };
    }
    
    return {
      success: false,
      status: 0,
      error: 'Network error or unable to connect'
    };
  }

  private getErrorMessage(error: any): string {
    if (error.error?.error) {
      return error.error.error;
    }
    if (error.error?.message) {
      return error.error.message;
    }
    if (error.message) {
      return error.message;
    }
    return 'An unexpected error occurred';
  }

  clearResponse(type: string): void {
    switch (type) {
      case 'get':
        this.getResponse = null;
        break;
      case 'post':
        this.postResponse = null;
        break;
      case 'delete':
        this.deleteResponse = null;
        break;
    }
  }
}