import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITransport } from 'app/shared/model/transport.model';

type EntityResponseType = HttpResponse<ITransport>;
type EntityArrayResponseType = HttpResponse<ITransport[]>;

@Injectable({ providedIn: 'root' })
export class TransportService {
    private resourceUrl = SERVER_API_URL + 'api/transports';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/transports';

    constructor(private http: HttpClient) {}

    create(transport: ITransport): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(transport);
        return this.http
            .post<ITransport>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(transport: ITransport): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(transport);
        return this.http
            .put<ITransport>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITransport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITransport[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITransport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(transport: ITransport): ITransport {
        const copy: ITransport = Object.assign({}, transport, {
            createDate: transport.createDate != null && transport.createDate.isValid() ? transport.createDate.format(DATE_FORMAT) : null,
            plannedDeliveryDate:
                transport.plannedDeliveryDate != null && transport.plannedDeliveryDate.isValid()
                    ? transport.plannedDeliveryDate.format(DATE_FORMAT)
                    : null,
            deliveryDate:
                transport.deliveryDate != null && transport.deliveryDate.isValid() ? transport.deliveryDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
        res.body.plannedDeliveryDate = res.body.plannedDeliveryDate != null ? moment(res.body.plannedDeliveryDate) : null;
        res.body.deliveryDate = res.body.deliveryDate != null ? moment(res.body.deliveryDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((transport: ITransport) => {
            transport.createDate = transport.createDate != null ? moment(transport.createDate) : null;
            transport.plannedDeliveryDate = transport.plannedDeliveryDate != null ? moment(transport.plannedDeliveryDate) : null;
            transport.deliveryDate = transport.deliveryDate != null ? moment(transport.deliveryDate) : null;
        });
        return res;
    }
}
