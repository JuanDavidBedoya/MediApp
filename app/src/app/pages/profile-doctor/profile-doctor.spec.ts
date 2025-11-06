import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileDoctor } from './profile-doctor';

describe('ProfileDoctor', () => {
  let component: ProfileDoctor;
  let fixture: ComponentFixture<ProfileDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileDoctor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileDoctor);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
