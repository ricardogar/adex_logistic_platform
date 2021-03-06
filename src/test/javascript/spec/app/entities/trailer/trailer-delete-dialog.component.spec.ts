/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AdexPlatformTestModule } from '../../../test.module';
import { TrailerDeleteDialogComponent } from 'app/entities/trailer/trailer-delete-dialog.component';
import { TrailerService } from 'app/entities/trailer/trailer.service';

describe('Component Tests', () => {
    describe('Trailer Management Delete Component', () => {
        let comp: TrailerDeleteDialogComponent;
        let fixture: ComponentFixture<TrailerDeleteDialogComponent>;
        let service: TrailerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [TrailerDeleteDialogComponent]
            })
                .overrideTemplate(TrailerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TrailerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrailerService);
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
