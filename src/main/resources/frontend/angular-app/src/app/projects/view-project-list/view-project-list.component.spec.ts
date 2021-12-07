import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewProjectListComponent } from './view-project-list.component';

describe('ViewProjectListComponent', () => {
  let component: ViewProjectListComponent;
  let fixture: ComponentFixture<ViewProjectListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewProjectListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewProjectListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
