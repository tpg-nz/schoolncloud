export interface ISubject {
  id?: number;
  guid?: string;
  name?: string;
  overview?: string;
  level?: number;
}

export const defaultValue: Readonly<ISubject> = {};
