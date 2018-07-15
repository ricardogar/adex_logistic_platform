/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { TransportUpdateComponent } from 'app/entities/transport/transport-update.component';
import { TransportService } from 'app/entities/transport/transport.service';
import { Transport } from 'app/shared/model/transport.model';

describe('Component Tests', () => {
    describe('Transport Management Update Component', () => {
        let comp: TransportUpdateComponent;
        let fixture: ComponentFixture<TransportUpdateComponent>;
        let service: TransportService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [TransportUpdateComponent]
            })
                .overrideTemplate(TransportUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TransportUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransportService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Transport(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.transport = entity;
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
                    const entity = new Transport();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.transport = entity;
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
