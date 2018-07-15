import { Moment } from 'moment';
import { IDriver } from 'app/shared/model//driver.model';
import { ITruck } from 'app/shared/model//truck.model';
import { ITrailer } from 'app/shared/model//trailer.model';
import { ICompanyFactory } from 'app/shared/model//company-factory.model';
import { ICompanyMain } from 'app/shared/model//company-main.model';

export const enum TransportStatus {
    LOAD = 'LOAD',
    UNOLADING = 'UNOLADING',
    CANCEL = 'CANCEL',
    COMPLETED = 'COMPLETED',
    INVOICE = 'INVOICE',
    PAID = 'PAID'
}

export interface ITransport {
    id?: number;
    orderNumber?: string;
    createDate?: Moment;
    status?: TransportStatus;
    city?: string;
    postCode?: string;
    street?: string;
    houseNumber?: number;
    firstName?: string;
    lastName?: string;
    phoneNumber?: string;
    plannedDeliveryDate?: Moment;
    deliveryDate?: Moment;
    changingPlaceUnloading?: boolean;
    comments?: string;
    driver?: IDriver;
    truck?: ITruck;
    trailer?: ITrailer;
    factory?: ICompanyFactory;
    comapny?: ICompanyMain;
}

export class Transport implements ITransport {
    constructor(
        public id?: number,
        public orderNumber?: string,
        public createDate?: Moment,
        public status?: TransportStatus,
        public city?: string,
        public postCode?: string,
        public street?: string,
        public houseNumber?: number,
        public firstName?: string,
        public lastName?: string,
        public phoneNumber?: string,
        public plannedDeliveryDate?: Moment,
        public deliveryDate?: Moment,
        public changingPlaceUnloading?: boolean,
        public comments?: string,
        public driver?: IDriver,
        public truck?: ITruck,
        public trailer?: ITrailer,
        public factory?: ICompanyFactory,
        public comapny?: ICompanyMain
    ) {
        this.changingPlaceUnloading = false;
    }
}
