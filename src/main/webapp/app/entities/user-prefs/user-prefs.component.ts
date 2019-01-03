import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserPrefs } from 'app/shared/model/user-prefs.model';
import { AccountService } from 'app/core';
import { UserPrefsService } from './user-prefs.service';

@Component({
    selector: 'jhi-user-prefs',
    templateUrl: './user-prefs.component.html'
})
export class UserPrefsComponent implements OnInit, OnDestroy {
    userPrefs: IUserPrefs[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected userPrefsService: UserPrefsService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.userPrefsService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IUserPrefs[]>) => (this.userPrefs = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.userPrefsService.query().subscribe(
            (res: HttpResponse<IUserPrefs[]>) => {
                this.userPrefs = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInUserPrefs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUserPrefs) {
        return item.id;
    }

    registerChangeInUserPrefs() {
        this.eventSubscriber = this.eventManager.subscribe('userPrefsListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
