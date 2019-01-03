/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { WorkstackTestModule } from '../../../test.module';
import { UserPrefsDeleteDialogComponent } from 'app/entities/user-prefs/user-prefs-delete-dialog.component';
import { UserPrefsService } from 'app/entities/user-prefs/user-prefs.service';

describe('Component Tests', () => {
    describe('UserPrefs Management Delete Component', () => {
        let comp: UserPrefsDeleteDialogComponent;
        let fixture: ComponentFixture<UserPrefsDeleteDialogComponent>;
        let service: UserPrefsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [WorkstackTestModule],
                declarations: [UserPrefsDeleteDialogComponent]
            })
                .overrideTemplate(UserPrefsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserPrefsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserPrefsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
