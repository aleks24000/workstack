import { IUserPrefs } from 'app/shared/model//user-prefs.model';
import { IDeliverable } from 'app/shared/model//deliverable.model';

export interface IProject {
    id?: number;
    techId?: string;
    name?: string;
    userPrefs?: IUserPrefs;
    deliverables?: IDeliverable[];
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public techId?: string,
        public name?: string,
        public userPrefs?: IUserPrefs,
        public deliverables?: IDeliverable[]
    ) {}
}
