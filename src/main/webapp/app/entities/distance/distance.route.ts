import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Distance } from 'app/shared/model/distance.model';
import { DistanceService } from './distance.service';
import { DistanceComponent } from './distance.component';
import { DistanceDetailComponent } from './distance-detail.component';
import { DistanceUpdateComponent } from './distance-update.component';
import { DistanceDeletePopupComponent } from './distance-delete-dialog.component';
import { IDistance } from 'app/shared/model/distance.model';

@Injectable({ providedIn: 'root' })
export class DistanceResolve implements Resolve<IDistance> {
    constructor(private service: DistanceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((distance: HttpResponse<Distance>) => distance.body));
        }
        return of(new Distance());
    }
}

export const distanceRoute: Routes = [
    {
        path: 'distance',
        component: DistanceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.distance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'distance/:id/view',
        component: DistanceDetailComponent,
        resolve: {
            distance: DistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.distance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'distance/new',
        component: DistanceUpdateComponent,
        resolve: {
            distance: DistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.distance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'distance/:id/edit',
        component: DistanceUpdateComponent,
        resolve: {
            distance: DistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.distance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const distancePopupRoute: Routes = [
    {
        path: 'distance/:id/delete',
        component: DistanceDeletePopupComponent,
        resolve: {
            distance: DistanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.distance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
