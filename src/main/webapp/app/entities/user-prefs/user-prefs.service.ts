import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserPrefs } from 'app/shared/model/user-prefs.model';

type EntityResponseType = HttpResponse<IUserPrefs>;
type EntityArrayResponseType = HttpResponse<IUserPrefs[]>;

@Injectable({ providedIn: 'root' })
export class UserPrefsService {
    public resourceUrl = SERVER_API_URL + 'api/user-prefs';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/user-prefs';

    constructor(protected http: HttpClient) {}

    create(userPrefs: IUserPrefs): Observable<EntityResponseType> {
        return this.http.post<IUserPrefs>(this.resourceUrl, userPrefs, { observe: 'response' });
    }

    update(userPrefs: IUserPrefs): Observable<EntityResponseType> {
        return this.http.put<IUserPrefs>(this.resourceUrl, userPrefs, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IUserPrefs>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUserPrefs[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUserPrefs[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
