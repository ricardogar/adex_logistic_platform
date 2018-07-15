import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IDistance } from 'app/shared/model/distance.model';
import { DistanceService } from './distance.service';
import { IDriver } from 'app/shared/model/driver.model';
import { DriverService } from 'app/entities/driver';
import { ITruck } from 'app/shared/model/truck.model';
import { TruckService } from 'app/entities/truck';

@Component({
    selector: 'jhi-distance-update',
    templateUrl: './distance-update.component.html'
})
export class DistanceUpdateComponent implements OnInit {
    private _distance: IDistance;
    isSaving: boolean;

    drivers: IDriver[];

    trucks: ITruck[];
    startDayDp: any;
    endDayDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private distanceService: DistanceService,
        private driverService: DriverService,
        private truckService: TruckService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ distance }) => {
            this.distance = distance;
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
        if (this.distance.id !== undefined) {
            this.subscribeToSaveResponse(this.distanceService.update(this.distance));
        } else {
            this.subscribeToSaveResponse(this.distanceService.create(this.distance));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDistance>>) {
        result.subscribe((res: HttpResponse<IDistance>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get distance() {
        return this._distance;
    }

    set distance(distance: IDistance) {
        this._distance = distance;
    }
}
