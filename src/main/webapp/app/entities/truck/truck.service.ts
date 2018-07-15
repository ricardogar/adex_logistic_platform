import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITruck } from 'app/shared/model/truck.model';

type EntityResponseType = HttpResponse<ITruck>;
type EntityArrayResponseType = HttpResponse<ITruck[]>;

@Injectable({ providedIn: 'root' })
export class TruckService {
    private resourceUrl = SERVER_API_URL + 'api/trucks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/trucks';

    constructor(private http: HttpClient) {}

    create(truck: ITruck): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(truck);
        return this.http
            .post<ITruck>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(truck: ITruck): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(truck);
        return this.http
            .put<ITruck>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITruck>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITruck[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITruck[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(truck: ITruck): ITruck {
        const copy: ITruck = Object.assign({}, truck, {
            technicalExamDate:
                truck.technicalExamDate != null && truck.technicalExamDate.isValid() ? truck.technicalExamDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.technicalExamDate = res.body.technicalExamDate != null ? moment(res.body.technicalExamDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((truck: ITruck) => {
            truck.technicalExamDate = truck.technicalExamDate != null ? moment(truck.technicalExamDate) : null;
        });
        return res;
    }
}
