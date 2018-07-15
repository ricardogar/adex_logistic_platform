import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ITruck } from 'app/shared/model//truck.model';
import { ITransport } from 'app/shared/model//transport.model';
import { IFuel } from 'app/shared/model//fuel.model';
import { IDistance } from 'app/shared/model//distance.model';

export interface IDriver {
    id?: number;
    pesel?: string;
    city?: string;
    postCode?: string;
    street?: string;
    houseNumber?: number;
    flatNumber?: number;
    phoneNumber?: string;
    medicalExamDate?: Moment;
    driverLicenceDate?: Moment;
    user?: IUser;
    truck?: ITruck;
    transports?: ITransport[];
    fuels?: IFuel[];
    distances?: IDistance[];
}

export class Driver implements IDriver {
    constructor(
        public id?: number,
        public pesel?: string,
        public city?: string,
        public postCode?: string,
        public street?: string,
        public houseNumber?: number,
        public flatNumber?: number,
        public phoneNumber?: string,
        public medicalExamDate?: Moment,
        public driverLicenceDate?: Moment,
        public user?: IUser,
        public truck?: ITruck,
        public transports?: ITransport[],
        public fuels?: IFuel[],
        public distances?: IDistance[]
    ) {}
}
