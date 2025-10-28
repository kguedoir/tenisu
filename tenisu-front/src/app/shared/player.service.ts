import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Player } from './player';
import { IPlayerService } from './player.service.port';
import { environment } from '../../environments/environment';
import {Observable} from 'rxjs';

// Service d'accès aux données des joueurs (implémentation en mémoire + tentative de synchronisation avec l'API)
// Fournit des méthodes de lecture et de recherche.
@Injectable({
  providedIn: 'root'
})
export class PlayerService implements IPlayerService {
  // URL de base des APIs, provenant de la configuration d'environnement
  private readonly baseUrl = environment.apisBaseUrl;
  private readonly playersEndpoint = this.baseUrl.replace(/\/$/, '') + '/players';
  constructor(private readonly httpClient: HttpClient) {
  }

  searchPlayers(query: string): Observable<Player[]> {
    const lowerQuery = query.toLowerCase();
    let params = new HttpParams();
    params = params.append('search', lowerQuery);
    return this.httpClient.get<Player[]>(this.playersEndpoint,{params});
  }

  getPlayerById(id: number): Observable<Player> {
    return this.httpClient.get<Player>(`${this.playersEndpoint}/${id}`);
  }

  stats(): Observable<any> {
    return this.httpClient.get<any>(`${this.playersEndpoint}/stats`);
  }
}
