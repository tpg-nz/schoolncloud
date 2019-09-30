import { IWorkflow } from 'app/shared/model/workflow.model';

export interface IStep {
  id?: number;
  sequence?: number;
  name?: string;
  workflow?: IWorkflow;
}

export const defaultValue: Readonly<IStep> = {};
