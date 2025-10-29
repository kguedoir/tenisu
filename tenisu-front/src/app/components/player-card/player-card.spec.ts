import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

import { PlayerCard } from './player-card';
import { Player } from '../../shared/player';

const mockPlayer: Player = {
  id: 42,
  firstname: 'Roger',
  lastname: 'Federer',
  shortname: 'RF',
  sex: 'M',
  country: { code: 'SUI', picture: '' },
  picture: 'https://example.com/rf.png',
  data: { rank: 1, points: 10000, weight: 85000, height: 185, age: 38, last: [1,1,1,1,1] }
};

describe('PlayerCard', () => {
  let component: PlayerCard;
  let fixture: ComponentFixture<PlayerCard>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerCard, RouterTestingModule]
    }).compileComponents();

    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(PlayerCard);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('player', mockPlayer);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render player name and stats', () => {
    const nameEl: HTMLElement = fixture.debugElement.query(By.css('.player-name')).nativeElement;
    expect(nameEl.textContent).toContain('Roger Federer');
    const rankEl: HTMLElement = fixture.debugElement.query(By.css('.stat-value.rank')).nativeElement;
    expect(rankEl.textContent?.trim()).toContain('#1');
    const pointsEl: HTMLElement = fixture.debugElement.query(By.css('.stat-value.points')).nativeElement;
    expect(pointsEl.textContent?.trim()).toBe('10000');
  });

  it('should navigate to detail on click', () => {
    const navigateSpy = spyOn(router, 'navigate');
    const button = fixture.debugElement.query(By.css('.player-card button'));
    expect(button).toBeTruthy();
    button.nativeElement.click();
    expect(navigateSpy).toHaveBeenCalledWith(['/player', 42]);
  });
});
