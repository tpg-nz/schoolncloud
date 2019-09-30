import { IWorkflow } from 'app/shared/model/workflow.model';

export interface IWorkflow {
  id?: number;
  name?: string;
  description?: string;
  enabled?: boolean;
  version?: string;
  workflow?: IWorkflow;
}

export const defaultValue: Readonly<IWorkflow> = {
  enabled: false
};
