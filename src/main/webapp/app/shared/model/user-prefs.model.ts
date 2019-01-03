import { IProject } from 'app/shared/model//project.model';

export interface IUserPrefs {
    id?: number;
    login?: string;
    projects?: IProject[];
}

export class UserPrefs implements IUserPrefs {
    constructor(public id?: number, public login?: string, public projects?: IProject[]) {}
}
