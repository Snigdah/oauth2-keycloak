import { Component, OnInit } from '@angular/core';
import { KeycloakService } from '../auth/keycloak.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  imports: [],
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class Landing implements OnInit{

  constructor(private keycloakService: KeycloakService, private route: Router) { }
  
   async ngOnInit(): Promise<void> {

   if(this.keycloakService.authenticated) {
       this.route.navigateByUrl("/home");
    } else {
       this.keycloakService.login();
    }}
  }
