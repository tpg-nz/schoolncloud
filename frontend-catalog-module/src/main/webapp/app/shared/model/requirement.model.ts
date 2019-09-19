import { ISubject } from 'app/shared/model/subject.model';
import { IPaper } from 'app/shared/model/paper.model';

export interface IRequirement {
  id?: number;
  level?: number;
  subject?: ISubject;
  paper?: IPaper;
}

export const defaultValue: Readonly<IRequirement> = {};
