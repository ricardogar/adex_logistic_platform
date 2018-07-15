import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CompanyFactory } from 'app/shared/model/company-factory.model';
import { CompanyFactoryService } from './company-factory.service';
import { CompanyFactoryComponent } from './company-factory.component';
import { CompanyFactoryDetailComponent } from './company-factory-detail.component';
import { CompanyFactoryUpdateComponent } from './company-factory-update.component';
import { CompanyFactoryDeletePopupComponent } from './company-factory-delete-dialog.component';
import { ICompanyFactory } from 'app/shared/model/company-factory.model';

@Injectable({ providedIn: 'root' })
export class CompanyFactoryResolve implements Resolve<ICompanyFactory> {
    constructor(private service: CompanyFactoryService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((companyFactory: HttpResponse<CompanyFactory>) => companyFactory.body));
        }
        return of(new CompanyFactory());
    }
}

export const companyFactoryRoute: Routes = [
    {
        path: 'company-factory',
        component: CompanyFactoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyFactory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'company-factory/:id/view',
        component: CompanyFactoryDetailComponent,
        resolve: {
            companyFactory: CompanyFactoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyFactory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'company-factory/new',
        component: CompanyFactoryUpdateComponent,
        resolve: {
            companyFactory: CompanyFactoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyFactory.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'company-factory/:id/edit',
        component: CompanyFactoryUpdateComponent,
        resolve: {
            companyFactory: CompanyFactoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyFactory.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const companyFactoryPopupRoute: Routes = [
    {
        path: 'company-factory/:id/delete',
        component: CompanyFactoryDeletePopupComponent,
        resolve: {
            companyFactory: CompanyFactoryResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'adexPlatformApp.companyFactory.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
