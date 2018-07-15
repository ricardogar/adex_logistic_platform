import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanyFactory } from 'app/shared/model/company-factory.model';

@Component({
    selector: 'jhi-company-factory-detail',
    templateUrl: './company-factory-detail.component.html'
})
export class CompanyFactoryDetailComponent implements OnInit {
    companyFactory: ICompanyFactory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ companyFactory }) => {
            this.companyFactory = companyFactory;
        });
    }

    previousState() {
        window.history.back();
    }
}
