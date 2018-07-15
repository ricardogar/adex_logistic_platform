import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICompanyMain } from 'app/shared/model/company-main.model';
import { CompanyMainService } from './company-main.service';

@Component({
    selector: 'jhi-company-main-delete-dialog',
    templateUrl: './company-main-delete-dialog.component.html'
})
export class CompanyMainDeleteDialogComponent {
    companyMain: ICompanyMain;

    constructor(
        private companyMainService: CompanyMainService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.companyMainService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'companyMainListModification',
                content: 'Deleted an companyMain'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-company-main-delete-popup',
    template: ''
})
export class CompanyMainDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ companyMain }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CompanyMainDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.companyMain = companyMain;
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
