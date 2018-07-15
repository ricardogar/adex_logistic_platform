import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITrailer } from 'app/shared/model/trailer.model';
import { TrailerService } from './trailer.service';
import { ITruck } from 'app/shared/model/truck.model';
import { TruckService } from 'app/entities/truck';

@Component({
    selector: 'jhi-trailer-update',
    templateUrl: './trailer-update.component.html'
})
export class TrailerUpdateComponent implements OnInit {
    private _trailer: ITrailer;
    isSaving: boolean;

    trucks: ITruck[];
    technicalExamDateDp: any;
    supervisionExamDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private trailerService: TrailerService,
        private truckService: TruckService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ trailer }) => {
            this.trailer = trailer;
        });
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
        if (this.trailer.id !== undefined) {
            this.subscribeToSaveResponse(this.trailerService.update(this.trailer));
        } else {
            this.subscribeToSaveResponse(this.trailerService.create(this.trailer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITrailer>>) {
        result.subscribe((res: HttpResponse<ITrailer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackTruckById(index: number, item: ITruck) {
        return item.id;
    }
    get trailer() {
        return this._trailer;
    }

    set trailer(trailer: ITrailer) {
        this._trailer = trailer;
    }
}
