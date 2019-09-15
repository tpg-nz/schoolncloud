import { IEducationalInstituition } from 'app/shared/model/educational-instituition.model';

export interface ICampus {
  id?: number;
  guid?: string;
  name?: string;
  educationalInstitution?: IEducationalInstituition;
}

export const defaultValue: Readonly<ICampus> = {};
