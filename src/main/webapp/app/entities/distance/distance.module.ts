import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    DistanceComponent,
    DistanceDetailComponent,
    DistanceUpdateComponent,
    DistanceDeletePopupComponent,
    DistanceDeleteDialogComponent,
    distanceRoute,
    distancePopupRoute
} from './';

const ENTITY_STATES = [...distanceRoute, ...distancePopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DistanceComponent,
        DistanceDetailComponent,
        DistanceUpdateComponent,
        DistanceDeleteDialogComponent,
        DistanceDeletePopupComponent
    ],
    entryComponents: [DistanceComponent, DistanceUpdateComponent, DistanceDeleteDialogComponent, DistanceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformDistanceModule {}
