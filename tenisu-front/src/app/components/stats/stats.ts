import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IPlayerService, PLAYER_SERVICE } from '../../shared/player.service.port';

@Component({
  selector: 'app-stats',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stats.html',
  styleUrls: ['./stats.scss']
})
export class Stats implements OnInit {
  stats: any = null;

  private readonly service = inject<IPlayerService>(PLAYER_SERVICE);

  ngOnInit(): void {
    this.service.stats().subscribe((s) => this.stats = s);
  }
}
