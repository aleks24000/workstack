import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliverable } from 'app/shared/model/deliverable.model';

@Component({
    selector: 'jhi-deliverable-detail',
    templateUrl: './deliverable-detail.component.html'
})
export class DeliverableDetailComponent implements OnInit {
    deliverable: IDeliverable;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ deliverable }) => {
            this.deliverable = deliverable;
        });
    }

    previousState() {
        window.history.back();
    }
}
