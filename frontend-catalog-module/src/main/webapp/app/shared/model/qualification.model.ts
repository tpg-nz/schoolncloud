import { ISubject } from 'app/shared/model/subject.model';

export interface IQualification {
  id?: number;
  guid?: string;
  name?: string;
  hyperLink?: string;
  subject?: ISubject;
}

export const defaultValue: Readonly<IQualification> = {};
