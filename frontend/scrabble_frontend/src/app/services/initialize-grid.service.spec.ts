import { TestBed } from '@angular/core/testing';

import { InitializeGridService } from './initialize-grid.service';

describe('InitializeGridService', () => {
  let service: InitializeGridService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InitializeGridService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
