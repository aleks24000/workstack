import { IDeliverable } from 'app/shared/model//deliverable.model';

export interface ITask {
    id?: number;
    name?: string;
    spentTime?: number;
    deliverable?: IDeliverable;
}

export class Task implements ITask {
    constructor(public id?: number, public name?: string, public spentTime?: number, public deliverable?: IDeliverable) {}
}
