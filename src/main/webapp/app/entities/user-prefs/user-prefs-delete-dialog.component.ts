import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserPrefs } from 'app/shared/model/user-prefs.model';
import { UserPrefsService } from './user-prefs.service';

@Component({
    selector: 'jhi-user-prefs-delete-dialog',
    templateUrl: './user-prefs-delete-dialog.component.html'
})
export class UserPrefsDeleteDialogComponent {
    userPrefs: IUserPrefs;

    constructor(
        protected userPrefsService: UserPrefsService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userPrefsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'userPrefsListModification',
                content: 'Deleted an userPrefs'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-prefs-delete-popup',
    template: ''
})
export class UserPrefsDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userPrefs }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(UserPrefsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.userPrefs = userPrefs;
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
