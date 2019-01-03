import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Deliverable } from 'app/shared/model/deliverable.model';
import { DeliverableService } from './deliverable.service';
import { DeliverableComponent } from './deliverable.component';
import { DeliverableDetailComponent } from './deliverable-detail.component';
import { DeliverableUpdateComponent } from './deliverable-update.component';
import { DeliverableDeletePopupComponent } from './deliverable-delete-dialog.component';
import { IDeliverable } from 'app/shared/model/deliverable.model';

@Injectable({ providedIn: 'root' })
export class DeliverableResolve implements Resolve<IDeliverable> {
    constructor(private service: DeliverableService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Deliverable> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Deliverable>) => response.ok),
                map((deliverable: HttpResponse<Deliverable>) => deliverable.body)
            );
        }
        return of(new Deliverable());
    }
}

export const deliverableRoute: Routes = [
    {
        path: 'deliverable',
        component: DeliverableComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.deliverable.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'deliverable/:id/view',
        component: DeliverableDetailComponent,
        resolve: {
            deliverable: DeliverableResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.deliverable.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'deliverable/new',
        component: DeliverableUpdateComponent,
        resolve: {
            deliverable: DeliverableResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.deliverable.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'deliverable/:id/edit',
        component: DeliverableUpdateComponent,
        resolve: {
            deliverable: DeliverableResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.deliverable.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const deliverablePopupRoute: Routes = [
    {
        path: 'deliverable/:id/delete',
        component: DeliverableDeletePopupComponent,
        resolve: {
            deliverable: DeliverableResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'workstackApp.deliverable.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
