import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentsUser } from './appointments-user';

describe('AppointmentsUser', () => {
  let component: AppointmentsUser;
  let fixture: ComponentFixture<AppointmentsUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentsUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentsUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
