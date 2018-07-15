/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { CompanyMainUpdateComponent } from 'app/entities/company-main/company-main-update.component';
import { CompanyMainService } from 'app/entities/company-main/company-main.service';
import { CompanyMain } from 'app/shared/model/company-main.model';

describe('Component Tests', () => {
    describe('CompanyMain Management Update Component', () => {
        let comp: CompanyMainUpdateComponent;
        let fixture: ComponentFixture<CompanyMainUpdateComponent>;
        let service: CompanyMainService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [CompanyMainUpdateComponent]
            })
                .overrideTemplate(CompanyMainUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CompanyMainUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CompanyMainService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new CompanyMain(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.companyMain = entity;
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
                    const entity = new CompanyMain();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.companyMain = entity;
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
