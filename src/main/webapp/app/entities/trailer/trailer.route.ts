import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Trailer } from 'app/shared/model/trailer.model';
import { TrailerService } from './trailer.service';
import { TrailerComponent } from './trailer.component';
import { TrailerDetailComponent } from './trailer-detail.component';
import { TrailerUpdateComponent } from './trailer-update.component';
import { TrailerDeletePopupComponent } from './trailer-delete-dialog.component';
import { ITrailer } from 'app/shared/model/trailer.model';

@Injectable({ providedIn: 'root' })
export class TrailerResolve implements Resolve<ITrailer> {
    constructor(private service: TrailerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((trailer: HttpResponse<Trailer>) => trailer.body));
        }
        return of(new Trailer());
    }
}

export const trailerRoute: Routes = [
    {
        path: 'trailer',
        component: TrailerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.trailer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'trailer/:id/view',
        component: TrailerDetailComponent,
        resolve: {
            trailer: TrailerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.trailer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'trailer/new',
        component: TrailerUpdateComponent,
        resolve: {
            trailer: TrailerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.trailer.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'trailer/:id/edit',
        component: TrailerUpdateComponent,
        resolve: {
            trailer: TrailerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.trailer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const trailerPopupRoute: Routes = [
    {
        path: 'trailer/:id/delete',
        component: TrailerDeletePopupComponent,
        resolve: {
            trailer: TrailerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.trailer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
