import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrailer } from 'app/shared/model/trailer.model';

@Component({
    selector: 'jhi-trailer-detail',
    templateUrl: './trailer-detail.component.html'
})
export class TrailerDetailComponent implements OnInit {
    trailer: ITrailer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trailer }) => {
            this.trailer = trailer;
        });
    }

    previousState() {
        window.history.back();
    }
}
