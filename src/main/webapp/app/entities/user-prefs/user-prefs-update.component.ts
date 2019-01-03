import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IUserPrefs } from 'app/shared/model/user-prefs.model';
import { UserPrefsService } from './user-prefs.service';

@Component({
    selector: 'jhi-user-prefs-update',
    templateUrl: './user-prefs-update.component.html'
})
export class UserPrefsUpdateComponent implements OnInit {
    userPrefs: IUserPrefs;
    isSaving: boolean;

    constructor(protected userPrefsService: UserPrefsService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userPrefs }) => {
            this.userPrefs = userPrefs;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userPrefs.id !== undefined) {
            this.subscribeToSaveResponse(this.userPrefsService.update(this.userPrefs));
        } else {
            this.subscribeToSaveResponse(this.userPrefsService.create(this.userPrefs));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPrefs>>) {
        result.subscribe((res: HttpResponse<IUserPrefs>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
