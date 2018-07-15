/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdexPlatformTestModule } from '../../../test.module';
import { TransportDetailComponent } from 'app/entities/transport/transport-detail.component';
import { Transport } from 'app/shared/model/transport.model';

describe('Component Tests', () => {
    describe('Transport Management Detail Component', () => {
        let comp: TransportDetailComponent;
        let fixture: ComponentFixture<TransportDetailComponent>;
        const route = ({ data: of({ transport: new Transport(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AdexPlatformTestModule],
                declarations: [TransportDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TransportDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TransportDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.transport).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
