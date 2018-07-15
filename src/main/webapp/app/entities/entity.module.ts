import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AdexPlatformDriverModule } from './driver/driver.module';
import { AdexPlatformTruckModule } from './truck/truck.module';
import { AdexPlatformTrailerModule } from './trailer/trailer.module';
import { AdexPlatformTransportModule } from './transport/transport.module';
import { AdexPlatformCompanyMainModule } from './company-main/company-main.module';
import { AdexPlatformCompanyFactoryModule } from './company-factory/company-factory.module';
import { AdexPlatformDistanceModule } from './distance/distance.module';
import { AdexPlatformFuelModule } from './fuel/fuel.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AdexPlatformDriverModule,
        AdexPlatformTruckModule,
        AdexPlatformTrailerModule,
        AdexPlatformTransportModule,
        AdexPlatformCompanyMainModule,
        AdexPlatformCompanyFactoryModule,
        AdexPlatformDistanceModule,
        AdexPlatformFuelModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AdexPlatformEntityModule {}
