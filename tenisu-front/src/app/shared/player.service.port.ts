import { InjectionToken } from '@angular/core';
import { Player } from './player';
import {Observable} from 'rxjs';

/**
 * Port de service pour l'accès aux joueurs.
 * Les composants dépendent de cette interface (DIP) plutôt que d'une implémentation concrète.
 */
export interface IPlayerService {
  /** Recherche des joueurs par prénom ou nom (insensible à la casse). */
  searchPlayers(query: string): Observable<Player[]>;
  /** Récupère un joueur par identifiant, ou undefined s'il n'existe pas. */
  getPlayerById(id: number): Observable<Player>;

  stats(): Observable<any>;
}

/**
 * Jeton d'injection pour fournir/consommer IPlayerService via l'injecteur Angular.
 */
export const PLAYER_SERVICE = new InjectionToken<IPlayerService>('PLAYER_SERVICE');
