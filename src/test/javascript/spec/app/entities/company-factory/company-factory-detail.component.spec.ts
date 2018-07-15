/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { CompanyFactoryDetailComponent } from 'app/entities/company-factory/company-factory-detail.component';
import { CompanyFactory } from 'app/shared/model/company-factory.model';

describe('Component Tests', () => {
    describe('CompanyFactory Management Detail Component', () => {
        let comp: CompanyFactoryDetailComponent;
        let fixture: ComponentFixture<CompanyFactoryDetailComponent>;
        const route = ({ data: of({ companyFactory: new CompanyFactory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [CompanyFactoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CompanyFactoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CompanyFactoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.companyFactory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
