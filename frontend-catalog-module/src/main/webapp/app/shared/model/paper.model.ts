import { ISubject } from 'app/shared/model/subject.model';

export interface IPaper {
  id?: number;
  code?: string;
  year?: number;
  points?: number;
  teachingPeriod?: string;
  subject?: ISubject;
}

export const defaultValue: Readonly<IPaper> = {};
