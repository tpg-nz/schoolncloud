import { IEducationalInstitution } from 'app/shared/model/educational-institution.model';

export interface ICampus {
  id?: number;
  name?: string;
  educationalInstitution?: IEducationalInstitution;
}

export const defaultValue: Readonly<ICampus> = {};
