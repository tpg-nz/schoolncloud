export interface IWorkflow {
  id?: number;
  name?: string;
  description?: string;
  version?: string;
  enabled?: boolean;
}

export const defaultValue: Readonly<IWorkflow> = {
  enabled: false
};
