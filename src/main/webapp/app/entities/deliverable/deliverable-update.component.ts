import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IDeliverable } from 'app/shared/model/deliverable.model';
import { DeliverableService } from './deliverable.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
    selector: 'jhi-deliverable-update',
    templateUrl: './deliverable-update.component.html'
})
export class DeliverableUpdateComponent implements OnInit {
    deliverable: IDeliverable;
    isSaving: boolean;

    projects: IProject[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected deliverableService: DeliverableService,
        protected projectService: ProjectService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ deliverable }) => {
            this.deliverable = deliverable;
        });
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.deliverable.id !== undefined) {
            this.subscribeToSaveResponse(this.deliverableService.update(this.deliverable));
        } else {
            this.subscribeToSaveResponse(this.deliverableService.create(this.deliverable));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliverable>>) {
        result.subscribe((res: HttpResponse<IDeliverable>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }
}
