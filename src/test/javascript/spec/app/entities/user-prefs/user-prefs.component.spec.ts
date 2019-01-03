/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WorkstackTestModule } from '../../../test.module';
import { UserPrefsComponent } from 'app/entities/user-prefs/user-prefs.component';
import { UserPrefsService } from 'app/entities/user-prefs/user-prefs.service';
import { UserPrefs } from 'app/shared/model/user-prefs.model';

describe('Component Tests', () => {
    describe('UserPrefs Management Component', () => {
        let comp: UserPrefsComponent;
        let fixture: ComponentFixture<UserPrefsComponent>;
        let service: UserPrefsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkstackTestModule],
                declarations: [UserPrefsComponent],
                providers: []
            })
                .overrideTemplate(UserPrefsComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserPrefsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPrefsService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new UserPrefs(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.userPrefs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
