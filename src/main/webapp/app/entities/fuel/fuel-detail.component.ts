import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFuel } from 'app/shared/model/fuel.model';

@Component({
    selector: 'jhi-fuel-detail',
    templateUrl: './fuel-detail.component.html'
})
export class FuelDetailComponent implements OnInit {
    fuel: IFuel;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fuel }) => {
            this.fuel = fuel;
        });
    }

    previousState() {
        window.history.back();
    }
}
