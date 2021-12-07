import { TestBed } from '@angular/core/testing';

import { MockDataStoreService } from './mock-data-store.service';

describe('MockDataStoreService', () => {
  let service: MockDataStoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MockDataStoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
