/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { DistanceUpdateComponent } from 'app/entities/distance/distance-update.component';
import { DistanceService } from 'app/entities/distance/distance.service';
import { Distance } from 'app/shared/model/distance.model';

describe('Component Tests', () => {
    describe('Distance Management Update Component', () => {
        let comp: DistanceUpdateComponent;
        let fixture: ComponentFixture<DistanceUpdateComponent>;
        let service: DistanceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [DistanceUpdateComponent]
            })
                .overrideTemplate(DistanceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DistanceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DistanceService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Distance(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.distance = entity;
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
                    const entity = new Distance();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.distance = entity;
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
