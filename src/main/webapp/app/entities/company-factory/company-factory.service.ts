import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompanyFactory } from 'app/shared/model/company-factory.model';

type EntityResponseType = HttpResponse<ICompanyFactory>;
type EntityArrayResponseType = HttpResponse<ICompanyFactory[]>;

@Injectable({ providedIn: 'root' })
export class CompanyFactoryService {
    private resourceUrl = SERVER_API_URL + 'api/company-factories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/company-factories';

    constructor(private http: HttpClient) {}

    create(companyFactory: ICompanyFactory): Observable<EntityResponseType> {
        return this.http.post<ICompanyFactory>(this.resourceUrl, companyFactory, { observe: 'response' });
    }

    update(companyFactory: ICompanyFactory): Observable<EntityResponseType> {
        return this.http.put<ICompanyFactory>(this.resourceUrl, companyFactory, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICompanyFactory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompanyFactory[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompanyFactory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
