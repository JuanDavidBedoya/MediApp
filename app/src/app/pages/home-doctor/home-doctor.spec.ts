import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeDoctor } from './home-doctor';

describe('HomeDoctor', () => {
  let component: HomeDoctor;
  let fixture: ComponentFixture<HomeDoctor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeDoctor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeDoctor);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
