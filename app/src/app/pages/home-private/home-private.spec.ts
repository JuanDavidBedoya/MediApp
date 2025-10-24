import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePrivate } from './home-private';

describe('HomePrivate', () => {
  let component: HomePrivate;
  let fixture: ComponentFixture<HomePrivate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomePrivate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomePrivate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
