import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PlayerService } from './player.service';
import { environment } from '../../environments/environment';
import { Player } from './player';

describe('PlayerService', () => {
  let service: PlayerService;
  let httpMock: HttpTestingController;
  const base = environment.apisBaseUrl.replace(/\/$/, '');

  const samplePlayers: Player[] = [
    {
      id: 1,
      firstname: 'Rafael',
      lastname: 'NADAL',
      shortname: 'R.NAD',
      sex: 'M',
      country: { code: 'ESP', picture: '' },
      picture: '',
      data: { rank: 2, points: 8000, weight: 85000, height: 185, age: 38, last: [1,1,0,1,1] }
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PlayerService]
    });
    service = TestBed.inject(PlayerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('searchPlayers should GET /players with lowercased search param', () => {
    let result: Player[] | undefined;
    service.searchPlayers('NaD').subscribe(r => (result = r));

    const req = httpMock.expectOne((request) =>
      request.method === 'GET' && request.url === `${base}/players`
    );
    expect(req.request.params.get('search')).toBe('nad');

    req.flush(samplePlayers);
    expect(result).toEqual(samplePlayers);
  });

  it('getPlayerById should GET /players/:id', () => {
    let player: Player | undefined;
    service.getPlayerById(1).subscribe(r => (player = r));

    const req = httpMock.expectOne(`${base}/players/1`);
    expect(req.request.method).toBe('GET');

    req.flush(samplePlayers[0]);
    expect(player).toEqual(samplePlayers[0]);
  });

  it('stats should GET /players/stats', () => {
    let stats: any;
    service.stats().subscribe(r => (stats = r));

    const req = httpMock.expectOne(`${base}/players/stats`);
    expect(req.request.method).toBe('GET');

    const payload = { totalPlayers: 3, avgHeight: 185 };
    req.flush(payload);

    expect(stats).toEqual(payload);
  });
});
