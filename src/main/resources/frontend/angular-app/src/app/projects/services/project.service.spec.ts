import { TestBed } from '@angular/core/testing';

import { rulesService } from './project.service';

describe('rulesService', () => {
  let service: rulesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(rulesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
