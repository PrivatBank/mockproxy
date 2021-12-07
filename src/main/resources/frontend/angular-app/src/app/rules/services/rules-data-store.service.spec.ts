import { TestBed } from '@angular/core/testing';

import { RulesDataStoreService } from './rules-data-store.service';

describe('RulesDataStoreService', () => {
  let service: RulesDataStoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RulesDataStoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
