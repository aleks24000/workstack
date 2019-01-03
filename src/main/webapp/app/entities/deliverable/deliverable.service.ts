import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDeliverable } from 'app/shared/model/deliverable.model';

type EntityResponseType = HttpResponse<IDeliverable>;
type EntityArrayResponseType = HttpResponse<IDeliverable[]>;

@Injectable({ providedIn: 'root' })
export class DeliverableService {
    public resourceUrl = SERVER_API_URL + 'api/deliverables';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/deliverables';

    constructor(protected http: HttpClient) {}

    create(deliverable: IDeliverable): Observable<EntityResponseType> {
        return this.http.post<IDeliverable>(this.resourceUrl, deliverable, { observe: 'response' });
    }

    update(deliverable: IDeliverable): Observable<EntityResponseType> {
        return this.http.put<IDeliverable>(this.resourceUrl, deliverable, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDeliverable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDeliverable[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDeliverable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
