import { IProject } from 'app/shared/model//project.model';
import { ITask } from 'app/shared/model//task.model';

export interface IDeliverable {
    id?: number;
    techId?: string;
    name?: string;
    spentTime?: number;
    project?: IProject;
    tasks?: ITask[];
}

export class Deliverable implements IDeliverable {
    constructor(
        public id?: number,
        public techId?: string,
        public name?: string,
        public spentTime?: number,
        public project?: IProject,
        public tasks?: ITask[]
    ) {}
}
