import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserPrefs } from 'app/shared/model/user-prefs.model';

@Component({
    selector: 'jhi-user-prefs-detail',
    templateUrl: './user-prefs-detail.component.html'
})
export class UserPrefsDetailComponent implements OnInit {
    userPrefs: IUserPrefs;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userPrefs }) => {
            this.userPrefs = userPrefs;
        });
    }

    previousState() {
        window.history.back();
    }
}
