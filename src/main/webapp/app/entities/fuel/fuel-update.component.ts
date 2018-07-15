import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFuel } from 'app/shared/model/fuel.model';
import { FuelService } from './fuel.service';
import { IDriver } from 'app/shared/model/driver.model';
import { DriverService } from 'app/entities/driver';
import { ITruck } from 'app/shared/model/truck.model';
import { TruckService } from 'app/entities/truck';

@Component({
    selector: 'jhi-fuel-update',
    templateUrl: './fuel-update.component.html'
})
export class FuelUpdateComponent implements OnInit {
    private _fuel: IFuel;
    isSaving: boolean;

    drivers: IDriver[];

    trucks: ITruck[];
    refuelDataDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private fuelService: FuelService,
        private driverService: DriverService,
        private truckService: TruckService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fuel }) => {
            this.fuel = fuel;
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
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fuel.id !== undefined) {
            this.subscribeToSaveResponse(this.fuelService.update(this.fuel));
        } else {
            this.subscribeToSaveResponse(this.fuelService.create(this.fuel));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFuel>>) {
        result.subscribe((res: HttpResponse<IFuel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get fuel() {
        return this._fuel;
    }

    set fuel(fuel: IFuel) {
        this._fuel = fuel;
    }
}
