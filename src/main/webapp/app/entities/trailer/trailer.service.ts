import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITrailer } from 'app/shared/model/trailer.model';

type EntityResponseType = HttpResponse<ITrailer>;
type EntityArrayResponseType = HttpResponse<ITrailer[]>;

@Injectable({ providedIn: 'root' })
export class TrailerService {
    private resourceUrl = SERVER_API_URL + 'api/trailers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/trailers';

    constructor(private http: HttpClient) {}

    create(trailer: ITrailer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(trailer);
        return this.http
            .post<ITrailer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(trailer: ITrailer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(trailer);
        return this.http
            .put<ITrailer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITrailer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITrailer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITrailer[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(trailer: ITrailer): ITrailer {
        const copy: ITrailer = Object.assign({}, trailer, {
            technicalExamDate:
                trailer.technicalExamDate != null && trailer.technicalExamDate.isValid()
                    ? trailer.technicalExamDate.format(DATE_FORMAT)
                    : null,
            supervisionExamDate:
                trailer.supervisionExamDate != null && trailer.supervisionExamDate.isValid()
                    ? trailer.supervisionExamDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.technicalExamDate = res.body.technicalExamDate != null ? moment(res.body.technicalExamDate) : null;
        res.body.supervisionExamDate = res.body.supervisionExamDate != null ? moment(res.body.supervisionExamDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((trailer: ITrailer) => {
            trailer.technicalExamDate = trailer.technicalExamDate != null ? moment(trailer.technicalExamDate) : null;
            trailer.supervisionExamDate = trailer.supervisionExamDate != null ? moment(trailer.supervisionExamDate) : null;
        });
        return res;
    }
}
