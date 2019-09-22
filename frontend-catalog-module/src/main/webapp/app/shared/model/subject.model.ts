export interface ISubject {
  id?: number;
  name?: string;
  overview?: string;
  level?: number;
}

export const defaultValue: Readonly<ISubject> = {};
