import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Truck } from 'app/shared/model/truck.model';
import { TruckService } from './truck.service';
import { TruckComponent } from './truck.component';
import { TruckDetailComponent } from './truck-detail.component';
import { TruckUpdateComponent } from './truck-update.component';
import { TruckDeletePopupComponent } from './truck-delete-dialog.component';
import { ITruck } from 'app/shared/model/truck.model';

@Injectable({ providedIn: 'root' })
export class TruckResolve implements Resolve<ITruck> {
    constructor(private service: TruckService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((truck: HttpResponse<Truck>) => truck.body));
        }
        return of(new Truck());
    }
}

export const truckRoute: Routes = [
    {
        path: 'truck',
        component: TruckComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.truck.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'truck/:id/view',
        component: TruckDetailComponent,
        resolve: {
            truck: TruckResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.truck.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'truck/new',
        component: TruckUpdateComponent,
        resolve: {
            truck: TruckResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.truck.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'truck/:id/edit',
        component: TruckUpdateComponent,
        resolve: {
            truck: TruckResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.truck.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const truckPopupRoute: Routes = [
    {
        path: 'truck/:id/delete',
        component: TruckDeletePopupComponent,
        resolve: {
            truck: TruckResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.truck.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
