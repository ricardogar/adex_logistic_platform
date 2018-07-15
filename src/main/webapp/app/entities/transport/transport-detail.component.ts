import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransport } from 'app/shared/model/transport.model';

@Component({
    selector: 'jhi-transport-detail',
    templateUrl: './transport-detail.component.html'
})
export class TransportDetailComponent implements OnInit {
    transport: ITransport;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ transport }) => {
            this.transport = transport;
        });
    }

    previousState() {
        window.history.back();
    }
}
