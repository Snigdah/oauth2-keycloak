import { Injectable } from '@angular/core';

Injectable({
  providedIn: 'root'
})
export const environment = {
  production: false,
  keycloak: {
    url: 'http://localhost:9080',
    realm: 'OneBank',
    clientId: 'ERP-Frontend'
  }
};
