/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkstackTestModule } from '../../../test.module';
import { UserPrefsDetailComponent } from 'app/entities/user-prefs/user-prefs-detail.component';
import { UserPrefs } from 'app/shared/model/user-prefs.model';

describe('Component Tests', () => {
    describe('UserPrefs Management Detail Component', () => {
        let comp: UserPrefsDetailComponent;
        let fixture: ComponentFixture<UserPrefsDetailComponent>;
        const route = ({ data: of({ userPrefs: new UserPrefs(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkstackTestModule],
                declarations: [UserPrefsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserPrefsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserPrefsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userPrefs).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
