import { IPaper } from 'app/shared/model/paper.model';

export const enum GraduationType {
  POST_GRADUATE = 'POST_GRADUATE',
  MASTER_DEGREE = 'MASTER_DEGREE',
  DOCTOR_DEGREE = 'DOCTOR_DEGREE',
  PHD = 'PHD'
}

export interface ITeachingStaff {
  id?: number;
  guid?: string;
  name?: string;
  graduationType?: GraduationType;
  papers?: IPaper[];
}

export const defaultValue: Readonly<ITeachingStaff> = {};
