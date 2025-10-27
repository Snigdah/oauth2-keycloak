import { Component, OnInit } from '@angular/core';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { KeycloakService } from '../auth/keycloak.service';

interface NavbarItem {
  name: string;
  uri: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,  // ✅ important for standalone component
  imports: [CommonModule, RouterModule, TitleCasePipe], // ✅ required modules and pipe
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar implements OnInit {
  navbarItems: NavbarItem[] = [];

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit(): Promise<void> {
    try {
       
      this.keycloakService.getAccessibleResources().subscribe((res : any) => {
        this.navbarItems = res.map((r: any) => ({
            name: r.name.replace(/hospital_navbar_/i, ''),
            uri: r.uris[0] || '/home'
        }))
      });
    } catch (error) {
      console.error('Error loading navbar resources', error);
    }
  }
}
