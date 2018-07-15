import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDriver } from 'app/shared/model/driver.model';

@Component({
    selector: 'jhi-driver-detail',
    templateUrl: './driver-detail.component.html'
})
export class DriverDetailComponent implements OnInit {
    driver: IDriver;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ driver }) => {
            this.driver = driver;
        });
    }

    previousState() {
        window.history.back();
    }
}
