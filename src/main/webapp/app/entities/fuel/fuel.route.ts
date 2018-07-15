import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Fuel } from 'app/shared/model/fuel.model';
import { FuelService } from './fuel.service';
import { FuelComponent } from './fuel.component';
import { FuelDetailComponent } from './fuel-detail.component';
import { FuelUpdateComponent } from './fuel-update.component';
import { FuelDeletePopupComponent } from './fuel-delete-dialog.component';
import { IFuel } from 'app/shared/model/fuel.model';

@Injectable({ providedIn: 'root' })
export class FuelResolve implements Resolve<IFuel> {
    constructor(private service: FuelService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((fuel: HttpResponse<Fuel>) => fuel.body));
        }
        return of(new Fuel());
    }
}

export const fuelRoute: Routes = [
    {
        path: 'fuel',
        component: FuelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.fuel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fuel/:id/view',
        component: FuelDetailComponent,
        resolve: {
            fuel: FuelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.fuel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fuel/new',
        component: FuelUpdateComponent,
        resolve: {
            fuel: FuelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.fuel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'fuel/:id/edit',
        component: FuelUpdateComponent,
        resolve: {
            fuel: FuelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.fuel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fuelPopupRoute: Routes = [
    {
        path: 'fuel/:id/delete',
        component: FuelDeletePopupComponent,
        resolve: {
            fuel: FuelResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.fuel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
