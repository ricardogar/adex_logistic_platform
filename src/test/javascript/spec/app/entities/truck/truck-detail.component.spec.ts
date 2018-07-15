/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { TruckDetailComponent } from 'app/entities/truck/truck-detail.component';
import { Truck } from 'app/shared/model/truck.model';

describe('Component Tests', () => {
    describe('Truck Management Detail Component', () => {
        let comp: TruckDetailComponent;
        let fixture: ComponentFixture<TruckDetailComponent>;
        const route = ({ data: of({ truck: new Truck(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [TruckDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TruckDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TruckDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.truck).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
