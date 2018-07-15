import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFuel } from 'app/shared/model/fuel.model';

type EntityResponseType = HttpResponse<IFuel>;
type EntityArrayResponseType = HttpResponse<IFuel[]>;

@Injectable({ providedIn: 'root' })
export class FuelService {
    private resourceUrl = SERVER_API_URL + 'api/fuels';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/fuels';

    constructor(private http: HttpClient) {}

    create(fuel: IFuel): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(fuel);
        return this.http
            .post<IFuel>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(fuel: IFuel): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(fuel);
        return this.http
            .put<IFuel>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IFuel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFuel[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IFuel[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(fuel: IFuel): IFuel {
        const copy: IFuel = Object.assign({}, fuel, {
            refuelData: fuel.refuelData != null && fuel.refuelData.isValid() ? fuel.refuelData.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.refuelData = res.body.refuelData != null ? moment(res.body.refuelData) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((fuel: IFuel) => {
            fuel.refuelData = fuel.refuelData != null ? moment(fuel.refuelData) : null;
        });
        return res;
    }
}
