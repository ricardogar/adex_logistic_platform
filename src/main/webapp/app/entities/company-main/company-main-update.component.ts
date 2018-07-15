import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ICompanyMain } from 'app/shared/model/company-main.model';
import { CompanyMainService } from './company-main.service';

@Component({
    selector: 'jhi-company-main-update',
    templateUrl: './company-main-update.component.html'
})
export class CompanyMainUpdateComponent implements OnInit {
    private _companyMain: ICompanyMain;
    isSaving: boolean;

    constructor(private companyMainService: CompanyMainService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ companyMain }) => {
            this.companyMain = companyMain;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.companyMain.id !== undefined) {
            this.subscribeToSaveResponse(this.companyMainService.update(this.companyMain));
        } else {
            this.subscribeToSaveResponse(this.companyMainService.create(this.companyMain));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICompanyMain>>) {
        result.subscribe((res: HttpResponse<ICompanyMain>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get companyMain() {
        return this._companyMain;
    }

    set companyMain(companyMain: ICompanyMain) {
        this._companyMain = companyMain;
    }
}
