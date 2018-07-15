import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICompanyFactory } from 'app/shared/model/company-factory.model';
import { CompanyFactoryService } from './company-factory.service';

@Component({
    selector: 'jhi-company-factory-delete-dialog',
    templateUrl: './company-factory-delete-dialog.component.html'
})
export class CompanyFactoryDeleteDialogComponent {
    companyFactory: ICompanyFactory;

    constructor(
        private companyFactoryService: CompanyFactoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.companyFactoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'companyFactoryListModification',
                content: 'Deleted an companyFactory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-company-factory-delete-popup',
    template: ''
})
export class CompanyFactoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ companyFactory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CompanyFactoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.companyFactory = companyFactory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
