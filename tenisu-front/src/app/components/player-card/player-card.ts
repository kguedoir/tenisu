import {Component, input} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {CountryNamePipe} from '../../pipes/country-name-pipe';
import {Router} from '@angular/router';
import {MatTooltip} from '@angular/material/tooltip';
import { Player } from '../../shared/player';

@Component({
  selector: 'app-player-card',
  standalone: true,
  imports: [CommonModule, CountryNamePipe, NgOptimizedImage, MatTooltip],
  templateUrl: './player-card.html',
  styleUrls: ['./player-card.scss'],
})
export class PlayerCard {
  // Entrée basée sur les signaux (Angular Signals API)
  // Rendu robuste dans les tests: pas "required" pour éviter NG0950 avant l'assignation
  player = input<Player>();
  isTouch = typeof globalThis !== 'undefined' && ('ontouchstart' in globalThis || navigator.maxTouchPoints > 0);

  constructor(private readonly router: Router) {}

  // Construit le nom complet du joueur
  getPlayerFullName(): string {
    const p = this.player();
    return p ? `${p.firstname} ${p.lastname}` : '';
  }

  // Navigue vers la page de détail du joueur
  navigateToDetail(): void {
    const p = this.player();
    if (!p) return;
    this.router.navigate(['/player', p.id]);
  }
}
