import { TestBed } from '@angular/core/testing';

import { HistgamesService } from './histgames.service';

describe('HistgamesService', () => {
  let service: HistgamesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistgamesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
