/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkstackTestModule } from '../../../test.module';
import { DeliverableDetailComponent } from 'app/entities/deliverable/deliverable-detail.component';
import { Deliverable } from 'app/shared/model/deliverable.model';

describe('Component Tests', () => {
    describe('Deliverable Management Detail Component', () => {
        let comp: DeliverableDetailComponent;
        let fixture: ComponentFixture<DeliverableDetailComponent>;
        const route = ({ data: of({ deliverable: new Deliverable(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkstackTestModule],
                declarations: [DeliverableDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DeliverableDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeliverableDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.deliverable).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
