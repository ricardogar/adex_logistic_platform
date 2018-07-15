import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITransport } from 'app/shared/model/transport.model';
import { TransportService } from './transport.service';
import { IDriver } from 'app/shared/model/driver.model';
import { DriverService } from 'app/entities/driver';
import { ITruck } from 'app/shared/model/truck.model';
import { TruckService } from 'app/entities/truck';
import { ITrailer } from 'app/shared/model/trailer.model';
import { TrailerService } from 'app/entities/trailer';
import { ICompanyFactory } from 'app/shared/model/company-factory.model';
import { CompanyFactoryService } from 'app/entities/company-factory';
import { ICompanyMain } from 'app/shared/model/company-main.model';
import { CompanyMainService } from 'app/entities/company-main';

@Component({
    selector: 'jhi-transport-update',
    templateUrl: './transport-update.component.html'
})
export class TransportUpdateComponent implements OnInit {
    private _transport: ITransport;
    isSaving: boolean;

    drivers: IDriver[];

    trucks: ITruck[];

    trailers: ITrailer[];

    companyfactories: ICompanyFactory[];

    companymains: ICompanyMain[];
    createDateDp: any;
    plannedDeliveryDateDp: any;
    deliveryDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private transportService: TransportService,
        private driverService: DriverService,
        private truckService: TruckService,
        private trailerService: TrailerService,
        private companyFactoryService: CompanyFactoryService,
        private companyMainService: CompanyMainService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ transport }) => {
            this.transport = transport;
        });
        this.driverService.query().subscribe(
            (res: HttpResponse<IDriver[]>) => {
                this.drivers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.truckService.query().subscribe(
            (res: HttpResponse<ITruck[]>) => {
                this.trucks = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.trailerService.query().subscribe(
            (res: HttpResponse<ITrailer[]>) => {
                this.trailers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.companyFactoryService.query().subscribe(
            (res: HttpResponse<ICompanyFactory[]>) => {
                this.companyfactories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.companyMainService.query().subscribe(
            (res: HttpResponse<ICompanyMain[]>) => {
                this.companymains = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.transport.id !== undefined) {
            this.subscribeToSaveResponse(this.transportService.update(this.transport));
        } else {
            this.subscribeToSaveResponse(this.transportService.create(this.transport));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITransport>>) {
        result.subscribe((res: HttpResponse<ITransport>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDriverById(index: number, item: IDriver) {
        return item.id;
    }

    trackTruckById(index: number, item: ITruck) {
        return item.id;
    }

    trackTrailerById(index: number, item: ITrailer) {
        return item.id;
    }

    trackCompanyFactoryById(index: number, item: ICompanyFactory) {
        return item.id;
    }

    trackCompanyMainById(index: number, item: ICompanyMain) {
        return item.id;
    }
    get transport() {
        return this._transport;
    }

    set transport(transport: ITransport) {
        this._transport = transport;
    }
}
