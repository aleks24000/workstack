import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserPrefs } from 'app/shared/model/user-prefs.model';
import { UserPrefsService } from './user-prefs.service';
import { UserPrefsComponent } from './user-prefs.component';
import { UserPrefsDetailComponent } from './user-prefs-detail.component';
import { UserPrefsUpdateComponent } from './user-prefs-update.component';
import { UserPrefsDeletePopupComponent } from './user-prefs-delete-dialog.component';
import { IUserPrefs } from 'app/shared/model/user-prefs.model';

@Injectable({ providedIn: 'root' })
export class UserPrefsResolve implements Resolve<IUserPrefs> {
    constructor(private service: UserPrefsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserPrefs> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<UserPrefs>) => response.ok),
                map((userPrefs: HttpResponse<UserPrefs>) => userPrefs.body)
            );
        }
        return of(new UserPrefs());
    }
}

export const userPrefsRoute: Routes = [
    {
        path: 'user-prefs',
        component: UserPrefsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.userPrefs.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-prefs/:id/view',
        component: UserPrefsDetailComponent,
        resolve: {
            userPrefs: UserPrefsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.userPrefs.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-prefs/new',
        component: UserPrefsUpdateComponent,
        resolve: {
            userPrefs: UserPrefsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.userPrefs.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'user-prefs/:id/edit',
        component: UserPrefsUpdateComponent,
        resolve: {
            userPrefs: UserPrefsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.userPrefs.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userPrefsPopupRoute: Routes = [
    {
        path: 'user-prefs/:id/delete',
        component: UserPrefsDeletePopupComponent,
        resolve: {
            userPrefs: UserPrefsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.userPrefs.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
