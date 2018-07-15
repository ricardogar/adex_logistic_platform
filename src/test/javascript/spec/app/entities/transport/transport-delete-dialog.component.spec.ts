/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AdexPlatformTestModule } from '../../../test.module';
import { TransportDeleteDialogComponent } from 'app/entities/transport/transport-delete-dialog.component';
import { TransportService } from 'app/entities/transport/transport.service';

describe('Component Tests', () => {
    describe('Transport Management Delete Component', () => {
        let comp: TransportDeleteDialogComponent;
        let fixture: ComponentFixture<TransportDeleteDialogComponent>;
        let service: TransportService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [TransportDeleteDialogComponent]
            })
                .overrideTemplate(TransportDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TransportDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TransportService);
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
