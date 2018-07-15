import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CompanyMain } from 'app/shared/model/company-main.model';
import { CompanyMainService } from './company-main.service';
import { CompanyMainComponent } from './company-main.component';
import { CompanyMainDetailComponent } from './company-main-detail.component';
import { CompanyMainUpdateComponent } from './company-main-update.component';
import { CompanyMainDeletePopupComponent } from './company-main-delete-dialog.component';
import { ICompanyMain } from 'app/shared/model/company-main.model';

@Injectable({ providedIn: 'root' })
export class CompanyMainResolve implements Resolve<ICompanyMain> {
    constructor(private service: CompanyMainService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((companyMain: HttpResponse<CompanyMain>) => companyMain.body));
        }
        return of(new CompanyMain());
    }
}

export const companyMainRoute: Routes = [
    {
        path: 'company-main',
        component: CompanyMainComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyMain.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'company-main/:id/view',
        component: CompanyMainDetailComponent,
        resolve: {
            companyMain: CompanyMainResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyMain.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'company-main/new',
        component: CompanyMainUpdateComponent,
        resolve: {
            companyMain: CompanyMainResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyMain.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'company-main/:id/edit',
        component: CompanyMainUpdateComponent,
        resolve: {
            companyMain: CompanyMainResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyMain.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const companyMainPopupRoute: Routes = [
    {
        path: 'company-main/:id/delete',
        component: CompanyMainDeletePopupComponent,
        resolve: {
            companyMain: CompanyMainResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyMain.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
