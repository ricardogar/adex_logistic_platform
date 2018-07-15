import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    TransportComponent,
    TransportDetailComponent,
    TransportUpdateComponent,
    TransportDeletePopupComponent,
    TransportDeleteDialogComponent,
    transportRoute,
    transportPopupRoute
} from './';

const ENTITY_STATES = [...transportRoute, ...transportPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TransportComponent,
        TransportDetailComponent,
        TransportUpdateComponent,
        TransportDeleteDialogComponent,
        TransportDeletePopupComponent
    ],
    entryComponents: [TransportComponent, TransportUpdateComponent, TransportDeleteDialogComponent, TransportDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformTransportModule {}
