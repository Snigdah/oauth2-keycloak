import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { environment } from '../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class KeycloakService {
    private _keycloak: Keycloak | undefined;
    private _authenticated = false;

    get keycloak(): Keycloak {
        if (!this._keycloak) {
            this._keycloak = new Keycloak({
                url: environment.keycloak.url,
                realm: environment.keycloak.realm,
                clientId: environment.keycloak.clientId
            });
        }
        return this._keycloak;
    }

    get authenticated(): boolean {
        return this._authenticated;
    }

   async init(): Promise<boolean> {
        this._authenticated = await this.keycloak.init({
            onLoad: 'check-sso',
            // onLoad: 'login-required',
            pkceMethod: 'S256',
            checkLoginIframe: true,
            checkLoginIframeInterval: 5
        });


        // 🔔 Listen for global logout

         this.keycloak.onAuthLogout = () => {
            console.log('Detected logout from another client');
            this.login();
        };
    

        return this._authenticated;
    }

    login(): void {
        this.keycloak.login({
            redirectUri: window.location.origin + '/landing'
        });
    }

    
    logout(): void {
        this.keycloak.logout({
            redirectUri: 'http://localhost:4000'
        });
    }

    getToken(): string {
        return this.keycloak.token || '';
    }
}

