import { ISubject } from 'app/shared/model/subject.model';

export interface IRequirement {
  id?: number;
  guid?: string;
  level?: number;
  subject?: ISubject;
}

export const defaultValue: Readonly<IRequirement> = {};
