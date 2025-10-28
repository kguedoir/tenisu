import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Home } from './home';
import { PLAYER_SERVICE, IPlayerService } from '../../shared/player.service.port';
import { Player } from '../../shared/player';
import { Observable, of } from 'rxjs';

class MockPlayerService implements IPlayerService {
  private all: Player[] = [
    { id: 1, firstname: 'Rafael', lastname: 'Nadal', shortname: 'RN', sex: 'M',
      country: { code: 'ESP', picture: '' }, picture: '', data: { rank: 2, points: 8000, weight: 85000, height: 185, age: 37, last: [1,1,0,1,1] } },
    { id: 2, firstname: 'Iga', lastname: 'Swiatek', shortname: 'IS', sex: 'F',
      country: { code: 'POL', picture: '' }, picture: '', data: { rank: 1, points: 9000, weight: 65000, height: 176, age: 23, last: [1,1,1,1,1] } },
    { id: 3, firstname: 'Novak', lastname: 'Djokovic', shortname: 'ND', sex: 'M',
      country: { code: 'SRB', picture: '' }, picture: '', data: { rank: 3, points: 7000, weight: 77000, height: 188, age: 36, last: [1,0,1,1,0] } },
  ];

  searchPlayers(query: string): Observable<Player[]> {
    const q = query.toLowerCase();
    return of(this.all.filter(p => (p.firstname + ' ' + p.lastname).toLowerCase().includes(q)));
  }

  getPlayerById(id: number): Observable<Player> {
    const found = this.all.find(p => p.id === id)!;
    return of(found);
  }

  stats(): Observable<any> {
    // Minimal mock stats object
    return of({ totalPlayers: this.all.length });
  }
}

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Home],
      providers: [
        { provide: PLAYER_SERVICE, useClass: MockPlayerService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load players on init', () => {
    expect(component.players.length).toBe(3);
    const cards = fixture.debugElement.queryAll(By.css('app-player-card'));
    expect(cards.length).toBe(3);
  });

  it('should filter players when search changes', () => {
    const inputDe = fixture.debugElement.query(By.css('input.custom-search-input'));
    const inputEl = inputDe.nativeElement as HTMLInputElement;

    inputEl.value = 'iga';
    inputEl.dispatchEvent(new Event('input'));

    // Bind two-way ngModel
    component.searchQuery = 'iga';
    component.onSearchTermChange();
    fixture.detectChanges();

    expect(component.players.length).toBe(1);
    const cards = fixture.debugElement.queryAll(By.css('app-player-card'));
    expect(cards.length).toBe(1);
  });
});
