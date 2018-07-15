import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AdexPlatformSharedModule } from 'app/shared';
import {
    FuelComponent,
    FuelDetailComponent,
    FuelUpdateComponent,
    FuelDeletePopupComponent,
    FuelDeleteDialogComponent,
    fuelRoute,
    fuelPopupRoute
} from './';

const ENTITY_STATES = [...fuelRoute, ...fuelPopupRoute];

@NgModule({
    imports: [AdexPlatformSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [FuelComponent, FuelDetailComponent, FuelUpdateComponent, FuelDeleteDialogComponent, FuelDeletePopupComponent],
    entryComponents: [FuelComponent, FuelUpdateComponent, FuelDeleteDialogComponent, FuelDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformFuelModule {}
