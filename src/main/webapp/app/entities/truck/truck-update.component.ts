import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITruck } from 'app/shared/model/truck.model';
import { TruckService } from './truck.service';
import { ITrailer } from 'app/shared/model/trailer.model';
import { TrailerService } from 'app/entities/trailer';
import { IDriver } from 'app/shared/model/driver.model';
import { DriverService } from 'app/entities/driver';

@Component({
    selector: 'jhi-truck-update',
    templateUrl: './truck-update.component.html'
})
export class TruckUpdateComponent implements OnInit {
    private _truck: ITruck;
    isSaving: boolean;

    trailers: ITrailer[];

    drivers: IDriver[];
    technicalExamDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private truckService: TruckService,
        private trailerService: TrailerService,
        private driverService: DriverService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ truck }) => {
            this.truck = truck;
        });
        this.trailerService.query({ filter: 'truck-is-null' }).subscribe(
            (res: HttpResponse<ITrailer[]>) => {
                if (!this.truck.trailer || !this.truck.trailer.id) {
                    this.trailers = res.body;
                } else {
                    this.trailerService.find(this.truck.trailer.id).subscribe(
                        (subRes: HttpResponse<ITrailer>) => {
                            this.trailers = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.driverService.query().subscribe(
            (res: HttpResponse<IDriver[]>) => {
                this.drivers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.truck.id !== undefined) {
            this.subscribeToSaveResponse(this.truckService.update(this.truck));
        } else {
            this.subscribeToSaveResponse(this.truckService.create(this.truck));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITruck>>) {
        result.subscribe((res: HttpResponse<ITruck>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackTrailerById(index: number, item: ITrailer) {
        return item.id;
    }

    trackDriverById(index: number, item: IDriver) {
        return item.id;
    }
    get truck() {
        return this._truck;
    }

    set truck(truck: ITruck) {
        this._truck = truck;
    }
}
