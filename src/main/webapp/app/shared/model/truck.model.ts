import { Moment } from 'moment';
import { ITrailer } from 'app/shared/model//trailer.model';
import { ITransport } from 'app/shared/model//transport.model';
import { IDriver } from 'app/shared/model//driver.model';
import { IFuel } from 'app/shared/model//fuel.model';
import { IDistance } from 'app/shared/model//distance.model';

export const enum EmissionStandard {
    EURO_3 = 'Euro 3',
    EURO_4 = 'Euro 4',
    EURO_5 = 'Euro 5',
    EURO_6 = 'Euro 6',
    EURO_7 = 'Euro 7'
}

export interface ITruck {
    id?: number;
    plate?: string;
    brand?: string;
    productionYear?: number;
    emissionStandard?: EmissionStandard;
    horsePower?: number;
    fuelTank?: number;
    technicalExamDate?: Moment;
    compressor?: boolean;
    hydraulics?: boolean;
    gps?: boolean;
    internationalLicense?: boolean;
    trailer?: ITrailer;
    transports?: ITransport[];
    driver?: IDriver;
    fuels?: IFuel[];
    distances?: IDistance[];
}

export class Truck implements ITruck {
    constructor(
        public id?: number,
        public plate?: string,
        public brand?: string,
        public productionYear?: number,
        public emissionStandard?: EmissionStandard,
        public horsePower?: number,
        public fuelTank?: number,
        public technicalExamDate?: Moment,
        public compressor?: boolean,
        public hydraulics?: boolean,
        public gps?: boolean,
        public internationalLicense?: boolean,
        public trailer?: ITrailer,
        public transports?: ITransport[],
        public driver?: IDriver,
        public fuels?: IFuel[],
        public distances?: IDistance[]
    ) {
        this.compressor = false;
        this.hydraulics = false;
        this.gps = false;
        this.internationalLicense = false;
    }
}
