import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectDetailsMainConteinerComponent } from './project-details-main-conteiner.component';

describe('ProjectDetailsMainConteinerComponent', () => {
  let component: ProjectDetailsMainConteinerComponent;
  let fixture: ComponentFixture<ProjectDetailsMainConteinerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProjectDetailsMainConteinerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectDetailsMainConteinerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
