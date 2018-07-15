import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import { AdexPlatformAdminModule } from 'app/admin/admin.module';
import {
    DriverComponent,
    DriverDetailComponent,
    DriverUpdateComponent,
    DriverDeletePopupComponent,
    DriverDeleteDialogComponent,
    driverRoute,
    driverPopupRoute
} from './';

const ENTITY_STATES = [...driverRoute, ...driverPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, AdexPlatformAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DriverComponent, DriverDetailComponent, DriverUpdateComponent, DriverDeleteDialogComponent, DriverDeletePopupComponent],
    entryComponents: [DriverComponent, DriverUpdateComponent, DriverDeleteDialogComponent, DriverDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformDriverModule {}
