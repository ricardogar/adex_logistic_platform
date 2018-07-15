import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Transport } from 'app/shared/model/transport.model';
import { TransportService } from './transport.service';
import { TransportComponent } from './transport.component';
import { TransportDetailComponent } from './transport-detail.component';
import { TransportUpdateComponent } from './transport-update.component';
import { TransportDeletePopupComponent } from './transport-delete-dialog.component';
import { ITransport } from 'app/shared/model/transport.model';

@Injectable({ providedIn: 'root' })
export class TransportResolve implements Resolve<ITransport> {
    constructor(private service: TransportService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((transport: HttpResponse<Transport>) => transport.body));
        }
        return of(new Transport());
    }
}

export const transportRoute: Routes = [
    {
        path: 'transport',
        component: TransportComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.transport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'transport/:id/view',
        component: TransportDetailComponent,
        resolve: {
            transport: TransportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.transport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'transport/new',
        component: TransportUpdateComponent,
        resolve: {
            transport: TransportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.transport.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'transport/:id/edit',
        component: TransportUpdateComponent,
        resolve: {
            transport: TransportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.transport.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transportPopupRoute: Routes = [
    {
        path: 'transport/:id/delete',
        component: TransportDeletePopupComponent,
        resolve: {
            transport: TransportResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.transport.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
