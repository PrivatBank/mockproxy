import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddMockComponent } from './add-mock.component';

describe('AddMockComponent', () => {
  let component: AddMockComponent;
  let fixture: ComponentFixture<AddMockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddMockComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddMockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
