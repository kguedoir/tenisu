import {Component, inject} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {CountryNamePipe} from '../../pipes/country-name-pipe';
import { Player } from '../../shared/player';
import { IPlayerService, PLAYER_SERVICE } from '../../shared/player.service.port';

@Component({
  selector: 'app-player-detail',
  standalone: true,
  imports: [CommonModule, CountryNamePipe, NgOptimizedImage],
  templateUrl: './player-detail.html',
  styleUrls: ['./player-detail.scss']
})
export class PlayerDetailComponent {
  // Joueur affiché dans la vue détail
  player?: Player;

  // Dépendances Angular injectées
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  // Service de joueurs injecté via le jeton (DIP)
  private readonly service = inject<IPlayerService>(PLAYER_SERVICE);

  // Récupère l'identifiant de l'URL et charge le joueur correspondant
  constructor() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam ? Number(idParam) : NaN;
    if (!isNaN(id)) {
      this.service.getPlayerById(id).subscribe(value => this.player = value);
    }
  }

  // Nom complet du joueur
  get fullName(): string {
    if (!this.player) return '';
    return `${this.player.firstname} ${this.player.lastname}`;
  }

  // Retour à la page d'accueil
  back(): void {
    this.router.navigateByUrl('/');
  }
}
