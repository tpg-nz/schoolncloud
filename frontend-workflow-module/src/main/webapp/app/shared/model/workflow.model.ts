import { IWorkflow } from 'app/shared/model/workflow.model';

export interface IWorkflow {
  id?: number;
  name?: string;
  description?: string;
  version?: string;
  enabled?: boolean;
  versionOf?: IWorkflow;
}

export const defaultValue: Readonly<IWorkflow> = {
  enabled: false
};
