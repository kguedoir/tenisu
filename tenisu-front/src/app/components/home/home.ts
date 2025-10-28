import {Component, OnInit, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PlayerCard} from '../player-card/player-card';
import {FormsModule} from '@angular/forms';
import { Player } from '../../shared/player';
import { IPlayerService, PLAYER_SERVICE } from '../../shared/player.service.port';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    PlayerCard,
    FormsModule,
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.scss'],
})
export class Home implements OnInit {
  // Liste des joueurs affichés
  players: Player[] = [];
  // Terme de recherche saisi par l'utilisateur
  searchQuery = '';
  // Statistiques récupérées depuis le service
  stats: any = null;

  // Service injecté via le jeton d'injection (DIP)
  private readonly service = inject<IPlayerService>(PLAYER_SERVICE);

  // Chargement initial de la liste des joueurs
  ngOnInit() {
   this.onSearchTermChange();
   this.service.stats().subscribe(stats => {
      this.stats = stats;
      console.log('Statistiques des joueurs :', stats);
   })
  }

  // Met à jour la liste selon le terme de recherche
  onSearchTermChange(): void {
   this.service.searchPlayers(this.searchQuery).subscribe(players => {this.players = players;});
  }
}


