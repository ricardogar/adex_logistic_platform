import { Moment } from 'moment';
import { IDriver } from 'app/shared/model//driver.model';
import { ITruck } from 'app/shared/model//truck.model';

export interface IFuel {
    id?: number;
    refuelData?: Moment;
    amountFuel?: number;
    kilometersState?: number;
    fuelPrice?: number;
    driver?: IDriver;
    truck?: ITruck;
}

export class Fuel implements IFuel {
    constructor(
        public id?: number,
        public refuelData?: Moment,
        public amountFuel?: number,
        public kilometersState?: number,
        public fuelPrice?: number,
        public driver?: IDriver,
        public truck?: ITruck
    ) {}
}
