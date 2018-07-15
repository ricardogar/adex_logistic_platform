import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    CompanyFactoryComponent,
    CompanyFactoryDetailComponent,
    CompanyFactoryUpdateComponent,
    CompanyFactoryDeletePopupComponent,
    CompanyFactoryDeleteDialogComponent,
    companyFactoryRoute,
    companyFactoryPopupRoute
} from './';

const ENTITY_STATES = [...companyFactoryRoute, ...companyFactoryPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CompanyFactoryComponent,
        CompanyFactoryDetailComponent,
        CompanyFactoryUpdateComponent,
        CompanyFactoryDeleteDialogComponent,
        CompanyFactoryDeletePopupComponent
    ],
    entryComponents: [
        CompanyFactoryComponent,
        CompanyFactoryUpdateComponent,
        CompanyFactoryDeleteDialogComponent,
        CompanyFactoryDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformCompanyFactoryModule {}
