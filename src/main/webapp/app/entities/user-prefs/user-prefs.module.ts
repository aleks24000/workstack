import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WorkstackSharedModule } from 'app/shared';
import {
    UserPrefsComponent,
    UserPrefsDetailComponent,
    UserPrefsUpdateComponent,
    UserPrefsDeletePopupComponent,
    UserPrefsDeleteDialogComponent,
    userPrefsRoute,
    userPrefsPopupRoute
} from './';

const ENTITY_STATES = [...userPrefsRoute, ...userPrefsPopupRoute];

@NgModule({
    imports: [WorkstackSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserPrefsComponent,
        UserPrefsDetailComponent,
        UserPrefsUpdateComponent,
        UserPrefsDeleteDialogComponent,
        UserPrefsDeletePopupComponent
    ],
    entryComponents: [UserPrefsComponent, UserPrefsUpdateComponent, UserPrefsDeleteDialogComponent, UserPrefsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstackUserPrefsModule {}
