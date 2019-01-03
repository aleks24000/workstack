/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WorkstackTestModule } from '../../../test.module';
import { DeliverableUpdateComponent } from 'app/entities/deliverable/deliverable-update.component';
import { DeliverableService } from 'app/entities/deliverable/deliverable.service';
import { Deliverable } from 'app/shared/model/deliverable.model';

describe('Component Tests', () => {
    describe('Deliverable Management Update Component', () => {
        let comp: DeliverableUpdateComponent;
        let fixture: ComponentFixture<DeliverableUpdateComponent>;
        let service: DeliverableService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkstackTestModule],
                declarations: [DeliverableUpdateComponent]
            })
                .overrideTemplate(DeliverableUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DeliverableUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DeliverableService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Deliverable(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.deliverable = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Deliverable();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.deliverable = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
