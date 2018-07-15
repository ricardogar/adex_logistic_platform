import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    CompanyMainComponent,
    CompanyMainDetailComponent,
    CompanyMainUpdateComponent,
    CompanyMainDeletePopupComponent,
    CompanyMainDeleteDialogComponent,
    companyMainRoute,
    companyMainPopupRoute
} from './';

const ENTITY_STATES = [...companyMainRoute, ...companyMainPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CompanyMainComponent,
        CompanyMainDetailComponent,
        CompanyMainUpdateComponent,
        CompanyMainDeleteDialogComponent,
        CompanyMainDeletePopupComponent
    ],
    entryComponents: [CompanyMainComponent, CompanyMainUpdateComponent, CompanyMainDeleteDialogComponent, CompanyMainDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformCompanyMainModule {}
