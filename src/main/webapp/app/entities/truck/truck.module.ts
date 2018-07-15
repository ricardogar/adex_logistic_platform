import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    TruckComponent,
    TruckDetailComponent,
    TruckUpdateComponent,
    TruckDeletePopupComponent,
    TruckDeleteDialogComponent,
    truckRoute,
    truckPopupRoute
} from './';

const ENTITY_STATES = [...truckRoute, ...truckPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [TruckComponent, TruckDetailComponent, TruckUpdateComponent, TruckDeleteDialogComponent, TruckDeletePopupComponent],
    entryComponents: [TruckComponent, TruckUpdateComponent, TruckDeleteDialogComponent, TruckDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformTruckModule {}
