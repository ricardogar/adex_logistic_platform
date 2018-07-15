import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    TrailerComponent,
    TrailerDetailComponent,
    TrailerUpdateComponent,
    TrailerDeletePopupComponent,
    TrailerDeleteDialogComponent,
    trailerRoute,
    trailerPopupRoute
} from './';

const ENTITY_STATES = [...trailerRoute, ...trailerPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TrailerComponent,
        TrailerDetailComponent,
        TrailerUpdateComponent,
        TrailerDeleteDialogComponent,
        TrailerDeletePopupComponent
    ],
    entryComponents: [TrailerComponent, TrailerUpdateComponent, TrailerDeleteDialogComponent, TrailerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformTrailerModule {}
