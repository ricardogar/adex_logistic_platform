import { ITransport } from 'app/shared/model//transport.model';
import { ICompanyFactory } from 'app/shared/model//company-factory.model';

export interface ICompanyMain {
    id?: number;
    nip?: string;
    name?: string;
    city?: string;
    postCode?: string;
    street?: string;
    houseNumber?: number;
    flatNumber?: number;
    email?: string;
    transports?: ITransport[];
    factories?: ICompanyFactory[];
}

export class CompanyMain implements ICompanyMain {
    constructor(
        public id?: number,
        public nip?: string,
        public name?: string,
        public city?: string,
        public postCode?: string,
        public street?: string,
        public houseNumber?: number,
        public flatNumber?: number,
        public email?: string,
        public transports?: ITransport[],
        public factories?: ICompanyFactory[]
    ) {}
}
