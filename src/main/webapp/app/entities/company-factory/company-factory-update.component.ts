import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICompanyFactory } from 'app/shared/model/company-factory.model';
import { CompanyFactoryService } from './company-factory.service';
import { ICompanyMain } from 'app/shared/model/company-main.model';
import { CompanyMainService } from 'app/entities/company-main';

@Component({
    selector: 'jhi-company-factory-update',
    templateUrl: './company-factory-update.component.html'
})
export class CompanyFactoryUpdateComponent implements OnInit {
    private _companyFactory: ICompanyFactory;
    isSaving: boolean;

    companymains: ICompanyMain[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private companyFactoryService: CompanyFactoryService,
        private companyMainService: CompanyMainService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ companyFactory }) => {
            this.companyFactory = companyFactory;
        });
        this.companyMainService.query().subscribe(
            (res: HttpResponse<ICompanyMain[]>) => {
                this.companymains = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.companyFactory.id !== undefined) {
            this.subscribeToSaveResponse(this.companyFactoryService.update(this.companyFactory));
        } else {
            this.subscribeToSaveResponse(this.companyFactoryService.create(this.companyFactory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyFactory>>) {
        result.subscribe((res: HttpResponse<ICompanyFactory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCompanyMainById(index: number, item: ICompanyMain) {
        return item.id;
    }
    get companyFactory() {
        return this._companyFactory;
    }

    set companyFactory(companyFactory: ICompanyFactory) {
        this._companyFactory = companyFactory;
    }
}
