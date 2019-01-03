/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { WorkstackTestModule } from '../../../test.module';
import { UserPrefsUpdateComponent } from 'app/entities/user-prefs/user-prefs-update.component';
import { UserPrefsService } from 'app/entities/user-prefs/user-prefs.service';
import { UserPrefs } from 'app/shared/model/user-prefs.model';

describe('Component Tests', () => {
    describe('UserPrefs Management Update Component', () => {
        let comp: UserPrefsUpdateComponent;
        let fixture: ComponentFixture<UserPrefsUpdateComponent>;
        let service: UserPrefsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkstackTestModule],
                declarations: [UserPrefsUpdateComponent]
            })
                .overrideTemplate(UserPrefsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserPrefsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPrefsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new UserPrefs(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userPrefs = entity;
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
                    const entity = new UserPrefs();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.userPrefs = entity;
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
