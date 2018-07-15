import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IDriver } from 'app/shared/model/driver.model';
import { DriverService } from './driver.service';
import { IUser, UserService } from 'app/core';
import { ITruck } from 'app/shared/model/truck.model';
import { TruckService } from 'app/entities/truck';

@Component({
    selector: 'jhi-driver-update',
    templateUrl: './driver-update.component.html'
})
export class DriverUpdateComponent implements OnInit {
    private _driver: IDriver;
    isSaving: boolean;

    users: IUser[];

    trucks: ITruck[];
    medicalExamDateDp: any;
    driverLicenceDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private driverService: DriverService,
        private userService: UserService,
        private truckService: TruckService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ driver }) => {
            this.driver = driver;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.truckService.query({ filter: 'driver-is-null' }).subscribe(
            (res: HttpResponse<ITruck[]>) => {
                if (!this.driver.truck || !this.driver.truck.id) {
                    this.trucks = res.body;
                } else {
                    this.truckService.find(this.driver.truck.id).subscribe(
                        (subRes: HttpResponse<ITruck>) => {
                            this.trucks = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.driver.id !== undefined) {
            this.subscribeToSaveResponse(this.driverService.update(this.driver));
        } else {
            this.subscribeToSaveResponse(this.driverService.create(this.driver));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IDriver>>) {
        result.subscribe((res: HttpResponse<IDriver>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackTruckById(index: number, item: ITruck) {
        return item.id;
    }
    get driver() {
        return this._driver;
    }

    set driver(driver: IDriver) {
        this._driver = driver;
    }
}
