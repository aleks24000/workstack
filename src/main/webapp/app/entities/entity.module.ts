import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { WorkstackUserPrefsModule } from './user-prefs/user-prefs.module';
import { WorkstackProjectModule } from './project/project.module';
import { WorkstackDeliverableModule } from './deliverable/deliverable.module';
import { WorkstackTaskModule } from './task/task.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        WorkstackUserPrefsModule,
        WorkstackProjectModule,
        WorkstackDeliverableModule,
        WorkstackTaskModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WorkstackEntityModule {}
