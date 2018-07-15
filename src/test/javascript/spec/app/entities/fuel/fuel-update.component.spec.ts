/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { FuelUpdateComponent } from 'app/entities/fuel/fuel-update.component';
import { FuelService } from 'app/entities/fuel/fuel.service';
import { Fuel } from 'app/shared/model/fuel.model';

describe('Component Tests', () => {
    describe('Fuel Management Update Component', () => {
        let comp: FuelUpdateComponent;
        let fixture: ComponentFixture<FuelUpdateComponent>;
        let service: FuelService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [FuelUpdateComponent]
            })
                .overrideTemplate(FuelUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FuelUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FuelService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Fuel(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.fuel = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Fuel();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.fuel = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
