import { ITransport } from 'app/shared/model//transport.model';
import { ICompanyMain } from 'app/shared/model//company-main.model';

export interface ICompanyFactory {
    id?: number;
    city?: string;
    postCode?: string;
    street?: string;
    houseNumber?: number;
    flatNumber?: number;
    phoneNumber?: string;
    transports?: ITransport[];
    company?: ICompanyMain;
}

export class CompanyFactory implements ICompanyFactory {
    constructor(
        public id?: number,
        public city?: string,
        public postCode?: string,
        public street?: string,
        public houseNumber?: number,
        public flatNumber?: number,
        public phoneNumber?: string,
        public transports?: ITransport[],
        public company?: ICompanyMain
    ) {}
}
