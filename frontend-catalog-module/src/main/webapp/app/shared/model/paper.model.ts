import { ISubject } from 'app/shared/model/subject.model';
import { ITeachingStaff } from 'app/shared/model/teaching-staff.model';

export interface IPaper {
  id?: number;
  code?: string;
  year?: number;
  points?: number;
  teachingPeriod?: string;
  subject?: ISubject;
  teachingStaffs?: ITeachingStaff[];
}

export const defaultValue: Readonly<IPaper> = {};
