/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AdexPlatformTestModule } from '../../../test.module';
import { CompanyFactoryDeleteDialogComponent } from 'app/entities/company-factory/company-factory-delete-dialog.component';
import { CompanyFactoryService } from 'app/entities/company-factory/company-factory.service';

describe('Component Tests', () => {
    describe('CompanyFactory Management Delete Component', () => {
        let comp: CompanyFactoryDeleteDialogComponent;
        let fixture: ComponentFixture<CompanyFactoryDeleteDialogComponent>;
        let service: CompanyFactoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [CompanyFactoryDeleteDialogComponent]
            })
                .overrideTemplate(CompanyFactoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompanyFactoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompanyFactoryService);
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
