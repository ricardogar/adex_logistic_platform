/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { CompanyMainDetailComponent } from 'app/entities/company-main/company-main-detail.component';
import { CompanyMain } from 'app/shared/model/company-main.model';

describe('Component Tests', () => {
    describe('CompanyMain Management Detail Component', () => {
        let comp: CompanyMainDetailComponent;
        let fixture: ComponentFixture<CompanyMainDetailComponent>;
        const route = ({ data: of({ companyMain: new CompanyMain(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [CompanyMainDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CompanyMainDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompanyMainDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.companyMain).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
