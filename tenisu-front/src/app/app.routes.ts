import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/home/home').then((m) => m.Home),
  },
  {
    path: 'stats',
    loadComponent: () => import('./components/stats/stats').then(m => m.Stats),
  },
  {
    path: 'player/:id',
    loadComponent: () => import('./components/player-detail/player-detail').then(m => m.PlayerDetailComponent),
  }
];
