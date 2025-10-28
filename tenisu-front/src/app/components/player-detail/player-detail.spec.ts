import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { PlayerDetailComponent } from './player-detail';
import { PLAYER_SERVICE, IPlayerService } from '../../shared/player.service.port';
import { Player } from '../../shared/player';

const mockPlayer: Player = {
  id: 1,
  firstname: 'Rafael',
  lastname: 'NADAL',
  shortname: 'R.NAD',
  sex: 'M',
  country: { code: 'ESP', picture: 'https://example.com/flag.png' },
  picture: 'https://example.com/nadal.png',
  data: { rank: 3, points: 7850, weight: 85000, height: 185, age: 38, last: [1,1,0,1,1] }
};

class MockPlayerService implements IPlayerService {
  searchPlayers() { return of([]); }
  getPlayerById(id: number) { return of(mockPlayer); }
  stats() { return of({}); }
}

describe('PlayerDetailComponent', () => {
  let component: PlayerDetailComponent;
  let fixture: ComponentFixture<PlayerDetailComponent>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerDetailComponent, RouterTestingModule],
      providers: [
        { provide: PLAYER_SERVICE, useClass: MockPlayerService },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: convertToParamMap({ id: '1' }) } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PlayerDetailComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load player on init and compute fullName', () => {
    expect(component.player).toEqual(mockPlayer);
    expect(component.fullName).toBe('Rafael NADAL');
  });

  it('should navigate back to home on back()', () => {
    const spy = spyOn(router, 'navigateByUrl');
    component.back();
    expect(spy).toHaveBeenCalledWith('/');
  });

  it('should render lastname in template', () => {
    const compiled: HTMLElement = fixture.nativeElement as HTMLElement;
    const lastNameEl = compiled.querySelector('.lastname');
    expect(lastNameEl?.textContent).toContain('NADAL');
  });
});
