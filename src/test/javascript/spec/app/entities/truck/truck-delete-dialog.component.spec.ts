/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AdexPlatformTestModule } from '../../../test.module';
import { TruckDeleteDialogComponent } from 'app/entities/truck/truck-delete-dialog.component';
import { TruckService } from 'app/entities/truck/truck.service';

describe('Component Tests', () => {
    describe('Truck Management Delete Component', () => {
        let comp: TruckDeleteDialogComponent;
        let fixture: ComponentFixture<TruckDeleteDialogComponent>;
        let service: TruckService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [TruckDeleteDialogComponent]
            })
                .overrideTemplate(TruckDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TruckDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TruckService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
