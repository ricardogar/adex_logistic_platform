import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDistance } from 'app/shared/model/distance.model';

type EntityResponseType = HttpResponse<IDistance>;
type EntityArrayResponseType = HttpResponse<IDistance[]>;

@Injectable({ providedIn: 'root' })
export class DistanceService {
    private resourceUrl = SERVER_API_URL + 'api/distances';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/distances';

    constructor(private http: HttpClient) {}

    create(distance: IDistance): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(distance);
        return this.http
            .post<IDistance>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(distance: IDistance): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(distance);
        return this.http
            .put<IDistance>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDistance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDistance[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDistance[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(distance: IDistance): IDistance {
        const copy: IDistance = Object.assign({}, distance, {
            startDay: distance.startDay != null && distance.startDay.isValid() ? distance.startDay.format(DATE_FORMAT) : null,
            endDay: distance.endDay != null && distance.endDay.isValid() ? distance.endDay.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDay = res.body.startDay != null ? moment(res.body.startDay) : null;
        res.body.endDay = res.body.endDay != null ? moment(res.body.endDay) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((distance: IDistance) => {
            distance.startDay = distance.startDay != null ? moment(distance.startDay) : null;
            distance.endDay = distance.endDay != null ? moment(distance.endDay) : null;
        });
        return res;
    }
}
