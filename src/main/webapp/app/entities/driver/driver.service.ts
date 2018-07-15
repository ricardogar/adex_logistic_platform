import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDriver } from 'app/shared/model/driver.model';

type EntityResponseType = HttpResponse<IDriver>;
type EntityArrayResponseType = HttpResponse<IDriver[]>;

@Injectable({ providedIn: 'root' })
export class DriverService {
    private resourceUrl = SERVER_API_URL + 'api/drivers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/drivers';

    constructor(private http: HttpClient) {}

    create(driver: IDriver): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(driver);
        return this.http
            .post<IDriver>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(driver: IDriver): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(driver);
        return this.http
            .put<IDriver>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IDriver>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDriver[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IDriver[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(driver: IDriver): IDriver {
        const copy: IDriver = Object.assign({}, driver, {
            medicalExamDate:
                driver.medicalExamDate != null && driver.medicalExamDate.isValid() ? driver.medicalExamDate.format(DATE_FORMAT) : null,
            driverLicenceDate:
                driver.driverLicenceDate != null && driver.driverLicenceDate.isValid() ? driver.driverLicenceDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.medicalExamDate = res.body.medicalExamDate != null ? moment(res.body.medicalExamDate) : null;
        res.body.driverLicenceDate = res.body.driverLicenceDate != null ? moment(res.body.driverLicenceDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((driver: IDriver) => {
            driver.medicalExamDate = driver.medicalExamDate != null ? moment(driver.medicalExamDate) : null;
            driver.driverLicenceDate = driver.driverLicenceDate != null ? moment(driver.driverLicenceDate) : null;
        });
        return res;
    }
}
