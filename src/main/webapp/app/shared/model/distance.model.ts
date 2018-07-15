import { Moment } from 'moment';
import { IDriver } from 'app/shared/model//driver.model';
import { ITruck } from 'app/shared/model//truck.model';

export interface IDistance {
    id?: number;
    startCity?: string;
    startDay?: Moment;
    startKilometers?: number;
    endCity?: string;
    endDay?: Moment;
    endKilometers?: number;
    driver?: IDriver;
    truck?: ITruck;
}

export class Distance implements IDistance {
    constructor(
        public id?: number,
        public startCity?: string,
        public startDay?: Moment,
        public startKilometers?: number,
        public endCity?: string,
        public endDay?: Moment,
        public endKilometers?: number,
        public driver?: IDriver,
        public truck?: ITruck
    ) {}
}
