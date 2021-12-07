import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMoksComponent } from './view-moks.component';

describe('ViewMoksComponent', () => {
  let component: ViewMoksComponent;
  let fixture: ComponentFixture<ViewMoksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewMoksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewMoksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
