import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

import { PLAYER_SERVICE } from './shared/player.service.port';
import { PlayerService } from './shared/player.service';

// Configuration globale de l'application Angular
// - Router
// - Gestion des erreurs globales
// - Détection des changements optimisée
// - Fourniture du service de joueurs via un jeton d'injection (DIP)
// - HttpClient pour les appels REST
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    { provide: PLAYER_SERVICE, useExisting: PlayerService }
  ]
};
