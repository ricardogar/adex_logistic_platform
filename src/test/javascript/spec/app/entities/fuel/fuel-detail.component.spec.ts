/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { FuelDetailComponent } from 'app/entities/fuel/fuel-detail.component';
import { Fuel } from 'app/shared/model/fuel.model';

describe('Component Tests', () => {
    describe('Fuel Management Detail Component', () => {
        let comp: FuelDetailComponent;
        let fixture: ComponentFixture<FuelDetailComponent>;
        const route = ({ data: of({ fuel: new Fuel(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [FuelDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FuelDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FuelDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.fuel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
