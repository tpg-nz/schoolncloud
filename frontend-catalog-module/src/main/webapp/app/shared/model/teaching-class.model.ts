import { ICampus } from 'app/shared/model/campus.model';
import { IPaper } from 'app/shared/model/paper.model';

export interface ITeachingClass {
  id?: number;
  code?: string;
  year?: number;
  semester?: number;
  campus?: ICampus;
  paper?: IPaper;
}

export const defaultValue: Readonly<ITeachingClass> = {};
