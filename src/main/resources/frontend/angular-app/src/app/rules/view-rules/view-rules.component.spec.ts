import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRulesComponent } from './view-rules.component';

describe('ViewRulesComponent', () => {
  let component: ViewRulesComponent;
  let fixture: ComponentFixture<ViewRulesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewRulesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewRulesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
