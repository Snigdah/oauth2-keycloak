// import { Injectable } from '@angular/core';
// import Keycloak from 'keycloak-js';
// import { environment } from '../environments/environment';

// @Injectable({
//     providedIn: 'root'
// })
// export class KeycloakService {
//     private _keycloak: Keycloak | undefined;
//     private _authenticated = false;

//     get keycloak(): Keycloak {
//         if (!this._keycloak) {
//             this._keycloak = new Keycloak({
//                 url: environment.keycloak.url,
//                 realm: environment.keycloak.realm,
//                 clientId: environment.keycloak.clientId
//             });
//         }
//         return this._keycloak;
//     }

//     get authenticated(): boolean {
//         return this._authenticated;
//     }

//     async init(): Promise<boolean> {
//         this._authenticated = await this.keycloak.init({
//             onLoad: 'check-sso',
//             pkceMethod: 'S256',
//             checkLoginIframe: false
//         });

//         return this._authenticated;
//     }

//     login(): void {
//         this.keycloak.login();
//     }

//     logout(): void {
//         this.keycloak.logout();
//     }

//     getToken(): string {
//         return this.keycloak.token || '';
//     }
// }

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
        console.log("without login clicked");

        try {
            this._authenticated = await this.keycloak.init({
                onLoad: 'check-sso',
                silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
                pkceMethod: 'S256',
                checkLoginIframe: false
            });

            return this._authenticated;
        } catch (error) {
            console.error('Keycloak init failed:', error);
            return false;
        }
    }

    async login(): Promise<void> {
        try {
            await this.keycloak.login({
                redirectUri: window.location.origin + '/home'
            });
        } catch (error) {
            console.error('Login failed:', error);
        }
    }

    logout(): void {
        this.keycloak.logout({
            redirectUri: window.location.origin + '/login'
        });
    }

    getToken(): string {
        return this.keycloak.token || '';
    }
}