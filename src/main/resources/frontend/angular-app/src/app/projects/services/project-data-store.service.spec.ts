import { TestBed } from '@angular/core/testing';

import { ProjectDataStoreService } from './project-data-store.service';

describe('ProjectDataStoreService', () => {
  let service: ProjectDataStoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectDataStoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
