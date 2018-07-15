import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICompanyMain } from 'app/shared/model/company-main.model';

type EntityResponseType = HttpResponse<ICompanyMain>;
type EntityArrayResponseType = HttpResponse<ICompanyMain[]>;

@Injectable({ providedIn: 'root' })
export class CompanyMainService {
    private resourceUrl = SERVER_API_URL + 'api/company-mains';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/company-mains';

    constructor(private http: HttpClient) {}

    create(companyMain: ICompanyMain): Observable<EntityResponseType> {
        return this.http.post<ICompanyMain>(this.resourceUrl, companyMain, { observe: 'response' });
    }

    update(companyMain: ICompanyMain): Observable<EntityResponseType> {
        return this.http.put<ICompanyMain>(this.resourceUrl, companyMain, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICompanyMain>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompanyMain[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICompanyMain[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
