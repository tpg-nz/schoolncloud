import { IUserRole } from 'app/shared/model/user-role.model';

export interface IRole {
  id?: number;
  guid?: string;
  name?: string;
  description?: string;
  userRoles?: IUserRole[];
}

export const defaultValue: Readonly<IRole> = {};
