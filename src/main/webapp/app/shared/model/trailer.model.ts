import { Moment } from 'moment';
import { ITransport } from 'app/shared/model//transport.model';
import { ITruck } from 'app/shared/model//truck.model';

export const enum TrailerType {
    CUARTAINSIDER = 'CUARTAINSIDER',
    LOWLOADER = 'LOWLOADER',
    DRY_BULK = 'DRY_BULK',
    SKELLY = 'SKELLY',
    TRIPPER = 'TRIPPER'
}

export interface ITrailer {
    id?: number;
    plate?: string;
    trailerType?: TrailerType;
    brand?: string;
    productionYear?: number;
    technicalExamDate?: Moment;
    supervisionExamDate?: Moment;
    maxCapacity?: number;
    transports?: ITransport[];
    truck?: ITruck;
}

export class Trailer implements ITrailer {
    constructor(
        public id?: number,
        public plate?: string,
        public trailerType?: TrailerType,
        public brand?: string,
        public productionYear?: number,
        public technicalExamDate?: Moment,
        public supervisionExamDate?: Moment,
        public maxCapacity?: number,
        public transports?: ITransport[],
        public truck?: ITruck
    ) {}
}
