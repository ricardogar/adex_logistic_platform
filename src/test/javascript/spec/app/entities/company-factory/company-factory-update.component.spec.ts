/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { CompanyFactoryUpdateComponent } from 'app/entities/company-factory/company-factory-update.component';
import { CompanyFactoryService } from 'app/entities/company-factory/company-factory.service';
import { CompanyFactory } from 'app/shared/model/company-factory.model';

describe('Component Tests', () => {
    describe('CompanyFactory Management Update Component', () => {
        let comp: CompanyFactoryUpdateComponent;
        let fixture: ComponentFixture<CompanyFactoryUpdateComponent>;
        let service: CompanyFactoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [CompanyFactoryUpdateComponent]
            })
                .overrideTemplate(CompanyFactoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CompanyFactoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompanyFactoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CompanyFactory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.companyFactory = entity;
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
                    const entity = new CompanyFactory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.companyFactory = entity;
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
