import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanyMain } from 'app/shared/model/company-main.model';

@Component({
    selector: 'jhi-company-main-detail',
    templateUrl: './company-main-detail.component.html'
})
export class CompanyMainDetailComponent implements OnInit {
    companyMain: ICompanyMain;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ companyMain }) => {
            this.companyMain = companyMain;
        });
    }

    previousState() {
        window.history.back();
    }
}
