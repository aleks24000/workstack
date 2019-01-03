import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstackSharedModule } from 'app/shared';
import {
    DeliverableComponent,
    DeliverableDetailComponent,
    DeliverableUpdateComponent,
    DeliverableDeletePopupComponent,
    DeliverableDeleteDialogComponent,
    deliverableRoute,
    deliverablePopupRoute
} from './';

const ENTITY_STATES = [...deliverableRoute, ...deliverablePopupRoute];

@NgModule({
    imports: [WorkstackSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DeliverableComponent,
        DeliverableDetailComponent,
        DeliverableUpdateComponent,
        DeliverableDeleteDialogComponent,
        DeliverableDeletePopupComponent
    ],
    entryComponents: [DeliverableComponent, DeliverableUpdateComponent, DeliverableDeleteDialogComponent, DeliverableDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstackDeliverableModule {}
