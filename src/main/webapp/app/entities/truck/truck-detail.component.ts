import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITruck } from 'app/shared/model/truck.model';

@Component({
    selector: 'jhi-truck-detail',
    templateUrl: './truck-detail.component.html'
})
export class TruckDetailComponent implements OnInit {
    truck: ITruck;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ truck }) => {
            this.truck = truck;
        });
    }

    previousState() {
        window.history.back();
    }
}
