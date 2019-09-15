import { IRole } from 'app/shared/model/role.model';

export interface IUserRole {
  id?: number;
  name?: string;
  roles?: IRole[];
}

export const defaultValue: Readonly<IUserRole> = {};
